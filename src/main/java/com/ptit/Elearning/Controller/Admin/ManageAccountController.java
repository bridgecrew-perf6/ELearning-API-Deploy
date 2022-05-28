package com.ptit.Elearning.Controller.Admin;

import com.ptit.Elearning.Config.JavaSenderService;
import com.ptit.Elearning.Constant.ERole;
import com.ptit.Elearning.DTO.AccountDTO;
import com.ptit.Elearning.DTO.AccountPageDTO.AccountAndUserInfo;
import com.ptit.Elearning.DTO.AccountPageDTO.AccountPageResponse;
import com.ptit.Elearning.DTO.RecoveryPkDTO.CodeVerifySuccess;
import com.ptit.Elearning.DTO.RecoveryPkDTO.HashCodeVerify;
import com.ptit.Elearning.DTO.RecoveryPkDTO.RecoveryModelRequest;
import com.ptit.Elearning.DTO.UserInfoDTO;
import com.ptit.Elearning.Entity.*;
import com.ptit.Elearning.Exception.NotFoundException;
import com.ptit.Elearning.Payloads.Request.Security.UpdatePasswordRequest;
import com.ptit.Elearning.Repository.UserInfoRespository;
import com.ptit.Elearning.Payloads.Request.LoginRequest;
import com.ptit.Elearning.Payloads.Request.SignupRequest;
import com.ptit.Elearning.Payloads.Response.JwtResponse;
import com.ptit.Elearning.Payloads.Response.MessageResponse;
import com.ptit.Elearning.Repository.AccountRepository;
import com.ptit.Elearning.Repository.RoleRepository;
import com.ptit.Elearning.Payloads.Request.Security.Jwt.JwtUtils;
import com.ptit.Elearning.Payloads.Request.Security.Service.UserDetailsImpl;
import com.ptit.Elearning.Service.*;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class ManageAccountController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserInfoRespository userInfoRespository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AccountService accountService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    TeacherService teacherService;

    @Autowired
    StudentService studentService;

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    JavaSenderService javaSenderService;

    @Autowired
    VerifyAccountService verifyAccountService;

    @ApiOperation(value = "Đăng nhập #1")
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        //add verify user status here

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles));
    }

    @ApiOperation("Đăng ký tạo tài khoản #77")
    @PostMapping("/verify-register-signup")
    public ResponseEntity<?> registerSignUp(@RequestParam("student-code") String studentCode, HttpSession session) {
        try {
            Student student = studentService.getByStudentCode(studentCode);
            try {
                //kiểm tra tài khoản của sinh viên này có tồn tại hay không? nếu có thì không thể đăng ký tiếp tục đk tạo tài khoản
                accountService.getByUserInfo(student.getUserInfo());
                return new ResponseEntity<>("Your have already had an account", HttpStatus.BAD_REQUEST);

            } catch (NotFoundException e) {
                VerifyAccount verifyAccount = new VerifyAccount();
                verifyAccount.setVerifyId(UUID.randomUUID());
                verifyAccount.setKey1(createCode());
                verifyAccount.setKey2(createCode());
                verifyAccount.setUserInfo(student.getUserInfo());
                verifyAccount = verifyAccountService.saveVerifyAccount(verifyAccount);

                javaSenderService.sendMailVerifyCode(student.getUserInfo().getEmail(), "Mã xác minh của bạn tại ELearning là: " + verifyAccount.getKey1(), 0);
                HashCodeVerify hash = new HashCodeVerify(verifyAccount.getVerifyId().toString());
                return ResponseEntity.ok(hash);
            }


        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @ApiOperation("Xác nhận mã khôi phục sau khi đăng ký tạo tài khoản #78")
    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@Valid @RequestBody RecoveryModelRequest request, HttpSession session) {
        try{

            VerifyAccount verifyAccount = verifyAccountService.getById(UUID.fromString(request.getKey()));
            if(!request.getCodeValue().equals(verifyAccount.getKey1().trim())) return ResponseEntity.badRequest().body("Verify code is not correct");

            //trả về verify code cho client
            CodeVerifySuccess hashCodeVerifySuccess  = new CodeVerifySuccess(verifyAccount.getVerifyId().toString(),verifyAccount.getKey2());

            return ResponseEntity.ok(hashCodeVerifySuccess);
        }catch (NotFoundException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }


    @ApiOperation(value = "Đăng ký tài khoản #0 \n Lưu ý: chức năng này chỉ dành cho sinh viên, vì tài khoản giảng viên là do nhà trường cấp")
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        VerifyAccount verifyAccount = new VerifyAccount();

        Student student;
        try{
            verifyAccount = verifyAccountService.getById(UUID.fromString(signUpRequest.getKey()));
            if(!verifyAccount.getKey2().equals(signUpRequest.getCodeValue().trim())) return ResponseEntity.badRequest().body("Key is invalid");

            student  = studentService.getByUserInfo(verifyAccount.getUserInfo());

        }catch (NotFoundException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }


        if (accountRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        UserInfo userInfo;
        try{
            userInfo = userInfoRespository.findUserInfoByUserId(student.getUserInfo().getUserId());
        }catch (NotFoundException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }


        // Create new user's account
        Account account = new Account(signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()), userInfo);

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByRoleName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            if (strRoles.size() == 0) return ResponseEntity.badRequest().body(new MessageResponse("Roles are empty!"));
            strRoles.forEach(role -> {
                switch (role) {
                    case "teacher":
                        Role adminRole = roleRepository.findByRoleName(ERole.ROLE_TEACHER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByRoleName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:

                        Role userRole = roleRepository.findByRoleName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        account.setRoles(roles);
        accountRepository.save(account);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @ApiOperation(value = "Quản lý thêm account #19")
    @PostMapping("/create-new-account")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> createNewAccount(@RequestParam(name = "user-id") Long userId, @Valid @RequestBody SignupRequest signUpRequest) {
        if (accountRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        //should use sesion instead
        UserInfo userInfo = userInfoRespository.findUserInfoByUserId(userId);

        //can't appear because we checked before.
        //should remove
        if (userInfo == null) return ResponseEntity.badRequest().body(new MessageResponse("User is not exist"));

        // Create new user's account
        Account account = new Account(signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()), userInfo);

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByRoleName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            if (strRoles.size() == 0) return ResponseEntity.badRequest().body(new MessageResponse("Roles are empty!"));
            strRoles.forEach(role -> {
                switch (role) {
                    case "teacher":
                        Role adminRole = roleRepository.findByRoleName(ERole.ROLE_TEACHER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByRoleName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:

                        Role userRole = roleRepository.findByRoleName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        account.setRoles(roles);
        accountRepository.save(account);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @ApiOperation(value = "Quản lý sửa password account #20")
    @PutMapping("/update-new-password")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdatePasswordRequest request) {
        if (request.getPassword().trim().length() < 6)
            return ResponseEntity.badRequest().body("The length of the password must be least at 6 charaters");
        if (!accountRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is not exist!"));
        }
        Account account = accountRepository.findByUsername(request.getUsername()).get();
        account.setPassword(encoder.encode(request.getPassword().trim()));
        accountRepository.save(account);
        return ResponseEntity.ok().body("Update password successfully!");
    }

    @ApiOperation(value = "Lấy thông tin tài khoản #31")
    @GetMapping("/get-account-info")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('TEACHER')")
    public ResponseEntity<?> getAccountInfo(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer")) {
            java.lang.String username = jwtUtils.getUserNameFromJwtToken(token.substring(7));
            Account account = accountService.getAccountByUsername(username);
            AccountDTO accountDTO = new AccountDTO();

            accountDTO.setAccountId(account.getAccountId());
            accountDTO.setRoles(account.getRoles().stream().map(role -> role.getRoleName().name()).collect(Collectors.toList()));
            accountDTO.setUserName(account.getUsername());
            return ResponseEntity.ok(accountDTO);
        }
        return new ResponseEntity<>("Failed to get User's infomation", HttpStatus.BAD_REQUEST);
    }

    @ApiOperation(value = "Lấy thông toàn bộ thông tin tài khoản người dùng #59")
    @GetMapping(value = {"/get-all-account-info", "/get-all-account-info/{pageNo}"})
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> getUserInfo(@PathVariable(required = false) Optional<Integer> pageNo) {
        int pageNoOffical = 1;
        if (pageNo.isPresent()) pageNoOffical = pageNo.get();
        int pageSize = 10;
        String sortDir = "desc";
        String sortField = "accountId";

        AccountPageResponse accountPageResponse = new AccountPageResponse();

        Page<Account> accountPage = accountService.getAllAccount(pageNoOffical, pageSize, sortField, sortDir);
        List<AccountAndUserInfo> accountAndUserInfos = convertToLisAccountAndtUserDTO(accountPage.getContent());

        accountPageResponse.setTotalPage(accountPage.getTotalPages());
        accountPageResponse.setAccountsInfo(accountAndUserInfos);

        return ResponseEntity.ok(accountPageResponse);
    }

    @ApiOperation(value = "Tìm kiếm thông tin người dùng bằng email, số điện thoại,họ tên #60")
    @GetMapping("/search-user-info")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> searchForUser(HttpServletRequest request, @RequestParam("key-search") String keySearch) {
        String token = request.getHeader("Authorization");
        List<UserInfo> userInfos = userInfoService.searchUser(keySearch);

        return new ResponseEntity<>(convertToListUserInfoDTO(userInfos), HttpStatus.BAD_REQUEST);
    }

    private List<AccountAndUserInfo> convertToLisAccountAndtUserDTO(List<Account> accounts) {

        List<AccountAndUserInfo> dtos = new ArrayList<>();

        accounts.forEach(account -> {
            AccountAndUserInfo info = new AccountAndUserInfo();


            UserInfo userInfo = account.getUserInfo();

            UserInfoDTO userInfoDTO = modelMapper.map(userInfo, UserInfoDTO.class);
            if (userInfo.getAvatar() != null) {
                userInfoDTO.setAvatar((ServletUriComponentsBuilder.fromCurrentContextPath().path("api/avatar/dowload/")
                        .path(userInfo.getAvatar().getAvatarId().toString())
                        .toUriString()));
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            if (userInfo.getDateOfBirth() != null)
                userInfoDTO.setDateOfBirth(simpleDateFormat.format(userInfo.getDateOfBirth()));

            try {
                Teacher teacher = teacherService.getByUserInfo(userInfo);

                userInfoDTO.setUserCode(teacher.getTeacherId());
                userInfoDTO.setUserClass("x");

            } catch (NotFoundException e) {
                Student student = studentService.getByUserInfo(userInfo);
                userInfoDTO.setUserCode(student.getStudentCode());
                userInfoDTO.setUserClass(student.getClassOf().getClassId());
            }
            userInfoDTO.setRoles(account.getRoles().stream().map(role -> role.getRoleName().name()).collect(Collectors.toList()));

            info.setAccountId(account.getAccountId());
            info.setUsername(account.getUsername());
            info.setUserInfoDTO(userInfoDTO);

            dtos.add(info);
        });

        return dtos;
    }

    private List<UserInfoDTO> convertToListUserInfoDTO(List<UserInfo> users) {
        List<UserInfoDTO> dtos = new ArrayList<>();
        users.forEach(u -> {
            UserInfoDTO userInfoDTO = modelMapper.map(u, UserInfoDTO.class);


            if (u.getAvatar() != null) {
                userInfoDTO.setAvatar((ServletUriComponentsBuilder.fromCurrentContextPath().path("api/avatar/dowload/")
                        .path(u.getAvatar().getAvatarId().toString())
                        .toUriString()));
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            if (u.getDateOfBirth() != null)
                userInfoDTO.setDateOfBirth(simpleDateFormat.format(u.getDateOfBirth()));

            try {
                Teacher teacher = teacherService.getByUserInfo(u);

                userInfoDTO.setUserCode(teacher.getTeacherId());
                userInfoDTO.setUserClass("x");

            } catch (NotFoundException e) {
                Student student = studentService.getByUserInfo(u);
                userInfoDTO.setUserCode(student.getStudentCode());
                userInfoDTO.setUserClass(student.getClassOf().getClassId());
            }

            try {
                Account account = accountService.getByUserInfo(u);
                userInfoDTO.setRoles(account.getRoles().stream().map(role -> role.getRoleName().name()).collect(Collectors.toList()));
            } catch (NotFoundException e) {
            }
            dtos.add(userInfoDTO);
        });
        return dtos;
    }
    private String createCode() {
        String code = "";
        Random rand = new Random();

        for (int i = 1; i <= 6; i++) {
            int tempIntCode = rand.nextInt(10);
            code += tempIntCode;
        }

        return code;
    }
}