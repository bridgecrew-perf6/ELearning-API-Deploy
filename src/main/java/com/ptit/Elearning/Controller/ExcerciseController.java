package com.ptit.Elearning.Controller;

import com.ptit.Elearning.AutogeneateNotification.NotificationForExcercise;
import com.ptit.Elearning.AutogeneateNotification.NotificationForJoinClass;
import com.ptit.Elearning.Constant.ERole;
import com.ptit.Elearning.DTO.DocumentDTOpk.DocumentDTO;
import com.ptit.Elearning.DTO.DocumentDTOpk.DocumentResponseDTODetail;
import com.ptit.Elearning.DTO.ExcerciseDTOpk.ExcerciseDTO;
import com.ptit.Elearning.DTO.ExcerciseDTOpk.ExcerciseRequestDTO;
import com.ptit.Elearning.Entity.*;
import com.ptit.Elearning.Exception.NotFoundException;
import com.ptit.Elearning.Payloads.Request.Security.Jwt.JwtUtils;
import com.ptit.Elearning.Repository.RoleRepository;
import com.ptit.Elearning.Service.*;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(value = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/excercise")
public class ExcerciseController {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AccountService accountService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    TeacherService teacherService;

    @Autowired
    ExcerciseService excerciseService;

    @Autowired
    ClassRegistrationService classRegistrationService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CreditClassService creditClassService;

    @Autowired
    DocumentService documentService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    StudentService studentService;

    @ApiOperation(value = "Thông tin tài liệu của bài tập #79")
    @GetMapping("/document-info")
    @PreAuthorize("hasRole('TEACHER') or hasRole('MODERATOR') or hasRole('USER')")
    public ResponseEntity<?> documentInfo(@RequestParam(name = "excercise-id") Long excerciseId, HttpServletRequest request) {
        CreditClass creditClass;
        Excercise excercise;

        try {
            excercise = excerciseService.getByExcerciseId(excerciseId);
            creditClass = excercise.getCreditClass();
        } catch (NotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        java.lang.String username = jwtUtils.getUserNameFromJwtToken(request.getHeader("Authorization").substring(7));
        Account account = accountService.getAccountByUsername(username);
        Teacher teacher = null;

        //nếu là giảng viên(không phải là moderator ) hoặc sinh viên thì phải dạy/học lớp đó mới được quyền xem file của sinh viên
        if (!isCanView(creditClass, account)) {
            return ResponseEntity.badRequest().body("You do not have permission!");
        }
        try {
            Set<Document> documents = excercise.getDocuments();
            Set<DocumentResponseDTODetail> dtos = new HashSet<>();
            documents.forEach(d -> {
                dtos.add(modelMapper.map(d, DocumentResponseDTODetail.class));
            });
            return ResponseEntity.ok(dtos);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation(value = "Lấy ra bài tập có id lớn nhất #84")
    @GetMapping("/max-id")
    @PreAuthorize("hasRole('TEACHER') or hasRole('MODERATOR') or hasRole('USER')")
    public ResponseEntity<?> documentInfo(HttpServletRequest request) {
        Excercise excercise= excerciseService.getExcerciseWithIdMax();
        ExcerciseDTO dto = new ExcerciseDTO();
        if(excercise!=null){
            dto.setDocuments(modelMapper.map(excercise.getDocuments(),new TypeToken<Set<DocumentResponseDTODetail>>(){}.getType()));
            dto.setExcerciseId(excercise.getExcerciseId());
            dto.setEndTime(excercise.getEndTime());
            dto.setStartTime(excercise.getStartTime());
            dto.setExcerciseContent(excercise.getExcerciseContent());
            dto.setTitle(excercise.getTitle());
            return ResponseEntity.ok(dto);

        }else{
            return new ResponseEntity<>("There is no excercise",HttpStatus.NOT_FOUND);
        }
    }
    @ApiOperation(value = "Tạo bài tập #85 Lưu ý: phải chi rõ contentType là: MULTIPART_FORM_DATA_VALUE")
    @PostMapping(value = "/create-new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('TEACHER') or hasRole('MODERATOR') or hasRole('USER')")
    public ResponseEntity<?> createNewExcercise(HttpServletRequest request, ExcerciseRequestDTO excerciseRequest) {
        CreditClass creditClass;

        try {
            creditClass = creditClassService.getCreditClassById(excerciseRequest.getCreditClassId());
        } catch (NotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        java.lang.String username = jwtUtils.getUserNameFromJwtToken(request.getHeader("Authorization").substring(7));
        Account account = accountService.getAccountByUsername(username);

        //nếu là giảng viên(không phải là moderator ) thì phải dạy lớp đó mới được quyền thêm bài tập
        if (!isCanAddNewExcercisePermition(creditClass, account)) {
            return ResponseEntity.badRequest().body("You do not have permission!");
        }
        try {
           Excercise excercise = new Excercise();

           excercise.setExcerciseContent(excerciseRequest.getExcerciseContent());
           excercise.setCreditClass(creditClass);
           excercise.setEndTime(excerciseRequest.getEndTime());
           excercise.setStartTime(excerciseRequest.getStartTime());
           excercise.setTitle(excerciseRequest.getTitle());

           Set<Document> documents = new HashSet<>();
           if(excerciseRequest.getFiles()!=null && excerciseRequest.getFiles().length>0){
               for (MultipartFile f : excerciseRequest.getFiles()){
                   Document document = documentService.saveDocument(f);
                   documents.add(document);
               }
           }
           excercise.setDocuments(documents);
           Excercise savedEx  = excerciseService.saveExcercise(excercise);

            List<Student> students = classRegistrationService.getListUserInfosByCreditClass(creditClass).stream().map(u->studentService.getByUserInfo(u)).collect(Collectors.toList());
            students.forEach(s->{
                Notification notification = new Notification();
                notification.setStatus(false);
                notification.setTime(new Timestamp(System.currentTimeMillis()));
                notification.setOwner(s.getUserInfo());
                notification.setNotificationContent(NotificationForExcercise.newExcercise(creditClass.getSubject().getSubjectName(),new Timestamp(System.currentTimeMillis())));
                notificationService.createNewNotification(notification);
            });
            ExcerciseDTO dto = new ExcerciseDTO();
            dto.setDocuments(modelMapper.map(savedEx.getDocuments(),new TypeToken<Set<DocumentResponseDTODetail>>(){}.getType()));
            dto.setExcerciseId(savedEx.getExcerciseId());
            dto.setEndTime(savedEx.getEndTime());
            dto.setStartTime(savedEx.getStartTime());
            dto.setExcerciseContent(savedEx.getExcerciseContent());
            dto.setTitle(savedEx.getTitle());
           return ResponseEntity.ok(dto);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (SizeLimitExceededException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    private boolean isCanView(CreditClass creditClass, Account account) {
        Set<Role> roles = account.getRoles();
        Role roleTeacher = roleRepository.findByRoleName(ERole.ROLE_TEACHER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        Role roleMod = roleRepository.findByRoleName(ERole.ROLE_MODERATOR).orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        //nếu là admin: được quyền xem
        if (roles.contains(roleMod)) return true;

        //nếu là giảng viên: phải là giảng viên dạy lớp đó
        if (roles.contains(roleTeacher)) {
            Teacher teacher = teacherService.getByUserInfo(account.getUserInfo());
            if (teacher.getCreditClasses().contains(creditClass)) return true;
        }

        //nếu là sinh viên: thì phải học lớp này !
        if (classRegistrationService.checkStudentJoinedClass(account.getUserInfo().getUserId(), creditClass.getCreditClassId()))
            return true;

        return false;
    }
    private boolean isCanAddNewExcercisePermition( CreditClass creditClass, Account account) {
        Set<Role> roles = account.getRoles();
        Role roleTeacher = roleRepository.findByRoleName(ERole.ROLE_TEACHER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        Role roleMod = roleRepository.findByRoleName(ERole.ROLE_MODERATOR).orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        //nếu là admin: được quyền thêm
        if(roles.contains(roleMod)) return true;

        //nếu là giảng viên: phải là giảng viên dạy lớp đó
        if(roles.contains(roleTeacher)){
            Teacher teacher = teacherService.getByUserInfo(account.getUserInfo());
            if(teacher.getCreditClasses().contains(creditClass)) return true;
        }

        return false;
    }
}
