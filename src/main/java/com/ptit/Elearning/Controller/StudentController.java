package com.ptit.Elearning.Controller;

import com.ptit.Elearning.Config.JavaSenderService;
import com.ptit.Elearning.DTO.RecoveryPkDTO.CodeVerifySuccess;
import com.ptit.Elearning.DTO.RecoveryPkDTO.HashCodeVerify;

import com.ptit.Elearning.DTO.RecoveryPkDTO.RecoveryModelRequest;
import com.ptit.Elearning.DTO.RecoveryPkDTO.UpdatePasswordRequestWithVerify;
import com.ptit.Elearning.DTO.StudentDTOpk.StudentDTO;
import com.ptit.Elearning.Entity.*;
import com.ptit.Elearning.Entity.Class;
import com.ptit.Elearning.Exception.NotFoundException;
import com.ptit.Elearning.Payloads.Response.MessageResponse;
import com.ptit.Elearning.Service.AccountService;
import com.ptit.Elearning.Service.ClassService;
import com.ptit.Elearning.Service.StudentService;
import com.ptit.Elearning.Service.VerifyAccountService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;

@CrossOrigin(value = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    StudentService studentService;

    @Autowired
    ClassService classService;

    @Autowired
    JavaSenderService javaSenderService;

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder encoder;


    @Autowired
    VerifyAccountService verifyAccountService;

    @ApiOperation("Lấy danh sách sinh viên theo lớp #54")
    @GetMapping("/get-all-by-class")
    public ResponseEntity<?> getAll(@RequestParam("class-id") String classId) {
        try {
            Class clas = classService.getByClassId(classId);
            List<Student> students = studentService.getByClass(clas);
            List<StudentDTO> dtos = convertToStudentDTO(students);

            return ResponseEntity.ok(dtos);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation("Tìm sinh viên theo mã sinh viên #55")
    @GetMapping("/get-by-student-code")
    public ResponseEntity<List<StudentDTO>> findByStudentCode(@RequestParam("student-code") String studentCode) {
        List<Student> students  = studentService.searchByStudentCode(studentCode);
        List<StudentDTO> dtos = convertToStudentDTO(students);
        return ResponseEntity.ok(dtos);
    }

    @ApiOperation("Xác nhận khôi phục lại mật khẩu #74")
    @PostMapping("/verify-forget-password")
    public ResponseEntity<?> verifyForgotPassword(@RequestParam("student-code") String studentCode, HttpSession session) {
        try{
            Student student  = studentService.getByStudentCode(studentCode);
            //kiểm tra tài khoản của sinh viên này có tồn tại hay không? nếu không thì không thể xác nhận quên mật khẩu
            accountService.getByUserInfo(student.getUserInfo());
            VerifyAccount verifyAccount=new VerifyAccount();
            try{
                verifyAccount= verifyAccountService.getByUserInfo(student.getUserInfo());
            }catch (NotFoundException e){
                //sinh viên đã tạo tài khoản rồi thì nhất định phải có verify account
                //đã check khi vừa vào(code bên dưới phục vụ cho môi trường test)
                //trong thực tế khi đã có tài khoản từ trước thì không thể vào code dưới đây
                verifyAccount.setVerifyId(UUID.randomUUID());

                verifyAccount.setUserInfo(student.getUserInfo());
            }
            verifyAccount.setKey1(createCode());
            verifyAccount.setKey2(createCode());
            verifyAccount = verifyAccountService.saveVerifyAccount(verifyAccount);

            javaSenderService.sendMailVerifyCode(student.getUserInfo().getEmail(),"Mã xác minh của bạn tại ELearning là: "+verifyAccount.getKey1(),2);


            HashCodeVerify hash = new HashCodeVerify(verifyAccount.getVerifyId().toString());
            return ResponseEntity.ok(hash);
        }catch (NotFoundException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);

        }
    }
    @ApiOperation("Xác nhận mã khôi phục sau khi nhận qua mail #75")
    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@Valid @RequestBody RecoveryModelRequest request, HttpSession session) {
        try{
            VerifyAccount verifyAccount = verifyAccountService.getById(UUID.fromString(request.getKey()));
            if(!request.getCodeValue().equals(verifyAccount.getKey1().toString())) return ResponseEntity.badRequest().body("Verify code is not correct");

            //trả về mã hash của verify code cho client
            CodeVerifySuccess hashCodeVerifySuccess  = new CodeVerifySuccess(verifyAccount.getVerifyId().toString(),verifyAccount.getKey2().toString());

            return ResponseEntity.ok(hashCodeVerifySuccess);
        }catch (NotFoundException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);

        }
    }

    @ApiOperation("Cập nhập mật khẩu #76")
    @PostMapping("/recover-password")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdatePasswordRequestWithVerify request, HttpSession session) {
        try{


            VerifyAccount verifyAccount = verifyAccountService.getById(UUID.fromString(request.getKey()));
            if(!verifyAccount.getKey2().toString().equals(request.getCodeValue().trim())) return ResponseEntity.badRequest().body("Key is invalid");

            Student student  = studentService.getByUserInfo(verifyAccount.getUserInfo());
            UserInfo userInfo = student.getUserInfo();
            Account account = accountService.getByUserInfo(userInfo);
            account.setPassword(encoder.encode(request.getPassword().trim()));
            accountService.saveAnExistingAccount(account);
            return ResponseEntity.ok("Cập nhật mật khẩu thành công!");
        }catch (NotFoundException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);

        }
    }

    public List<StudentDTO> convertToStudentDTO(List<Student> students){
        List<StudentDTO> dtos = new ArrayList<>();
        students.forEach(s->{
            StudentDTO dto = new StudentDTO();
            dto.setFullnanme(s.getUserInfo().getFullname());
            dto.setStudentCode(s.getStudentCode());
            dtos.add(dto);
        });
        return dtos;
    }
    String createCode() {
        String code = "";
        Random rand = new Random();

        for(int i =1;i<=6;i++) {
            int tempIntCode = rand.nextInt(10);
            code +=tempIntCode;
        }

        return code;
    }
}
