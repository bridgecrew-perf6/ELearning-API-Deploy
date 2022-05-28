package com.ptit.Elearning.Controller.Admin;

import com.ptit.Elearning.Constant.ERole;
import com.ptit.Elearning.DTO.DocumentDTOpk.DocumentResponseData;
import com.ptit.Elearning.DTO.MarkStudentPointDTO;
import com.ptit.Elearning.DTO.SubmitDTOpk.SubmitInventory;
import com.ptit.Elearning.DTO.SubmitDTOpk.SubmitRequest;
import com.ptit.Elearning.DTO.SubmitDTOpk.SubmitResponseDTO;
import com.ptit.Elearning.Entity.*;
import com.ptit.Elearning.Exception.NotFoundException;
import com.ptit.Elearning.Payloads.Request.Security.Jwt.JwtUtils;
import com.ptit.Elearning.Repository.RoleRepository;
import com.ptit.Elearning.Service.*;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(value ="*",maxAge = 3600)
@RestController
@RequestMapping("/api/submit")
public class SubmitController {


    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AccountService accountService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    TeacherService teacherService;

    @Autowired
    CreditClassService creditClassService;

    @Autowired
    ExcerciseService excerciseService;

    @Autowired
    SubmitService submitService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    StudentService studentService;

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    DocumentService documentService;
    /*
    chấm điểm sinh viên:
        phải có quyền giảng viên or moderator và dạy lớp đó
        lấy ra đúng submit theo : mã userid và mã excercise
        set mark cho submit đó
     */
    @ApiOperation(value="Chấm điểm cho bài tập #66")
    @PutMapping("/mark")
    @PreAuthorize("hasRole('TEACHER') or hasRole('MODERATOR')")
    public ResponseEntity<?> mark(HttpServletRequest request, @Valid @RequestBody  MarkStudentPointDTO markDTO){
        CreditClass creditClass;
        Excercise excercise;
        try{
            excercise = excerciseService.getByExcerciseId(markDTO.getExcerciseId());
            creditClass = excercise.getCreditClass();
        }catch(NotFoundException e){
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.NOT_FOUND);
        }

        java.lang.String username = jwtUtils.getUserNameFromJwtToken(request.getHeader("Authorization").substring(7));
        Account account = accountService.getAccountByUsername(username);
        Teacher teacher = null;

        //nếu là giảng viên(không phải là moderator ) thì phải dạy lớp đó mới được quyền nhập điểm
        if(isTeacherAndNotModerator(account)){
            teacher = teacherService.getByUserInfo(account.getUserInfo());
            if(!teacher.getCreditClasses().contains(creditClass)) return ResponseEntity.badRequest().body("You do not have permission!");
        }
        try{
            Submit submit = submitService.getSubmitWithExcerciseIdAndUserId(markDTO.getExcerciseId(),markDTO.getUserId());
            submit.setMark(markDTO.getMark());
            submitService.markStudentPoint(submit);
            return ResponseEntity.ok("Mark success!");
        }catch (NotFoundException e){
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @ApiOperation(value="Xem danh sách sinh viên đã nộp bài #67")
    @GetMapping("/get-list-submit")
    @PreAuthorize("hasRole('TEACHER') or hasRole('MODERATOR')")
    public ResponseEntity<?> getListStudentSubmit(HttpServletRequest request, @RequestParam long excerciseId){
        CreditClass creditClass;
        Excercise excercise;
        try{
            excercise = excerciseService.getByExcerciseId(excerciseId);
            creditClass = excercise.getCreditClass();
        }catch(NotFoundException e){
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.NOT_FOUND);
        }

        java.lang.String username = jwtUtils.getUserNameFromJwtToken(request.getHeader("Authorization").substring(7));
        Account account = accountService.getAccountByUsername(username);
        Teacher teacher = null;

        //nếu là giảng viên(không phải là moderator ) thì phải dạy lớp đó mới được quyền xem danh sách
        if(isTeacherAndNotModerator(account)){
            teacher = teacherService.getByUserInfo(account.getUserInfo());
            if(!teacher.getCreditClasses().contains(creditClass)) return ResponseEntity.badRequest().body("You do not have permission!");
        }
            List<Submit> submits = submitService.getSubmitWithExcerciseId(excerciseId);
            List<SubmitResponseDTO> submitResponseDTOS = submits.stream().map(s-> convertToSubmitDTO(s)).collect(Collectors.toList());
            Collections.sort(submitResponseDTOS, new SortSubmitDTOByStudentId());
            return ResponseEntity.ok(submitResponseDTOS);
    }

    @ApiOperation(value = "Thông tin nộp bài của sinh viên #69")
    @GetMapping("/submit-info")
    @PreAuthorize("hasRole('TEACHER') or hasRole('MODERATOR')")
    public ResponseEntity<?> inforSubmit(@RequestParam(name = "excercise-id") Long excerciseId,@RequestParam(name="user-id") Long userId, HttpServletRequest request) {
        CreditClass creditClass;
        Excercise excercise;
        try{
            excercise = excerciseService.getByExcerciseId(excerciseId);
            creditClass = excercise.getCreditClass();
        }catch(NotFoundException e){
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        java.lang.String username = jwtUtils.getUserNameFromJwtToken(request.getHeader("Authorization").substring(7));
        Account account = accountService.getAccountByUsername(username);
        Teacher teacher = null;

        //nếu là giảng viên(không phải là moderator ) thì phải dạy lớp đó mới được quyền xem bài nộp chi tiết của sinh viên
        if(isTeacherAndNotModerator(account)){
            teacher = teacherService.getByUserInfo(account.getUserInfo());
            if(!teacher.getCreditClasses().contains(creditClass)) return ResponseEntity.badRequest().body("You do not have permission!");
        }
        try {
            Submit submit = submitService.getSubmitWithExcerciseIdAndUserId(excerciseId, userId);
            return ResponseEntity.ok(convertToSubmitDTO(submit));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @ApiOperation(value = "Nộp bài tập #70 \n Lưu ý: phải chỉ rõ contenttype là:MULTIPART_FORM_DATA_VALUE ")
    @PreAuthorize("hasRole('USER')")
    @PostMapping(value="/index", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> submitExcercise( SubmitRequest submitRequest, HttpServletRequest request) {

        String username = jwtUtils.getUserNameFromJwtToken(request.getHeader("Authorization").substring(7));
        Account account = accountService.getAccountByUsername(username);

        try {
            Excercise excercise = excerciseService.getByExcerciseId(submitRequest.getExcerciseId());
            SubmitId submitId = new SubmitId(submitRequest.getExcerciseId(),account.getUserInfo().getUserId());
            Submit submit = new Submit();
            submit.setSubmitId(submitId);
            submit.setSubmitTime(new Timestamp(System.currentTimeMillis()));
            submit.setSubmitContent(submitRequest.getSubmitContent().trim());
            submit.setUserInfo(account.getUserInfo());
            submit.setExcercise(excercise);
            submit.setMark(0);
            if(submitRequest.getFile()!=null)
            {
                Document document = documentService.saveDocument(submitRequest.getFile());
                submit.setDocument(document);
            }

            return ResponseEntity.ok(convertToSubmitDTO(submitService.submit(submit)));
        } catch (NotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation(value = "Xóa bài đã nộp #71")
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping(value="/delete")
    public ResponseEntity<?> deleteExcercise( @RequestParam("excercise-id") Long excerciseId,  HttpServletRequest request) {

        String username = jwtUtils.getUserNameFromJwtToken(request.getHeader("Authorization").substring(7));
        Account account = accountService.getAccountByUsername(username);

        try {
            Excercise excercise = excerciseService.getByExcerciseId(excerciseId);
            Submit submit = submitService.getSubmitWithExcerciseIdAndUserId(excerciseId,account.getUserInfo().getUserId());
            boolean check = submitService.delete(submit);
            if(check)
            return ResponseEntity.ok("Delete successfully");

            return ResponseEntity.unprocessableEntity().build();

        } catch (NotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation(value="Thống kê điểm sinh viên #72")
    @GetMapping("/inventory")
    @PreAuthorize("hasRole('TEACHER') or hasRole('MODERATOR')")
    public ResponseEntity<?> inventorySubmit(HttpServletRequest request, @RequestParam("excercise-id") long excerciseId){
        CreditClass creditClass;
        Excercise excercise;
        try{
            excercise = excerciseService.getByExcerciseId(excerciseId);
            creditClass = excercise.getCreditClass();
        }catch(NotFoundException e){
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.NOT_FOUND);
        }

        java.lang.String username = jwtUtils.getUserNameFromJwtToken(request.getHeader("Authorization").substring(7));
        Account account = accountService.getAccountByUsername(username);
        Teacher teacher = null;

        //nếu là giảng viên(không phải là moderator ) thì phải dạy lớp đó mới được quyền xem danh sách
        if(isTeacherAndNotModerator(account)){
            teacher = teacherService.getByUserInfo(account.getUserInfo());
            if(!teacher.getCreditClasses().contains(creditClass)) return ResponseEntity.badRequest().body("You do not have permission!");
        }
        List<Submit> submits = submitService.getSubmitWithExcerciseId(excerciseId);
        float high = 0;
        float medium =0;
        float low = 0;
        for(Submit s: submits){
            if(s.getMark()>=8) high++;
            else if(s.getMark()>=6.5) medium++;
            else if(s.getMark()>=5) low++;
        }
        float highRatio = (float)Math.round(high/submits.size()*100)/100;
        float mediumRatio = (float)Math.round(medium/submits.size()*100)/100;
        float lowRatio = (float)Math.round(low/submits.size()*100)/100;

        SubmitInventory inventory = new SubmitInventory(highRatio,mediumRatio,lowRatio,1-highRatio-mediumRatio-lowRatio);
        return ResponseEntity.ok(inventory);
    }

    private boolean isTeacherAndNotModerator(Account account) {

        Set<Role> roles = account.getRoles();
        Role roleTeacher = roleRepository.findByRoleName(ERole.ROLE_TEACHER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        Role roleMod = roleRepository.findByRoleName(ERole.ROLE_MODERATOR).orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        if (roles.contains(roleTeacher) && !roles.contains(roleMod) ){
            return true;
        }


        return false;
    }
    private SubmitResponseDTO convertToSubmitDTO(Submit submit){
         SubmitResponseDTO submitDTO = modelMapper.map(submit,SubmitResponseDTO.class);

         submitDTO.setFullname(submit.getUserInfo().getFullname());
         if(submit.getDocument()!=null){
             submitDTO.setDocumentURL(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/admin/document/dowload/")
                     .path(submit.getDocument().getDocumentId().toString())
                     .toUriString());
         }
         if(submit.getUserInfo().getAvatar()!=null)
         submitDTO.setAvatarId(submit.getUserInfo().getAvatar().getAvatarId());
         submitDTO.setStudentCode(studentService.getByUserInfo(submit.getUserInfo()).getStudentCode());
        return submitDTO;
    }
    class SortSubmitDTOByStudentId implements Comparator<SubmitResponseDTO> {
        @Override
        public int compare(SubmitResponseDTO o1, SubmitResponseDTO o2) {
            return (int) (o1.getStudentCode().compareTo(o2.getStudentCode()));
        }
    }
}
