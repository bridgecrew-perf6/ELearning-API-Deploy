package com.ptit.Elearning.Controller.Admin;

import com.ptit.Elearning.Constant.ERole;
import com.ptit.Elearning.DTO.CreditclassDTOpk.*;
import com.ptit.Elearning.DTO.ListStudentIDDTO;
import com.ptit.Elearning.DTO.TimelineDTOpk.TimelineDTO;
import com.ptit.Elearning.Entity.*;
import com.ptit.Elearning.Exception.NotFoundException;
import com.ptit.Elearning.Payloads.Request.Security.Jwt.JwtUtils;
import com.ptit.Elearning.Repository.RoleRepository;
import com.ptit.Elearning.Service.*;
import io.swagger.annotations.ApiOperation;
import org.graalvm.compiler.api.replacements.Fold;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin/creditclass")
public class ManageCreditClass {
    @Autowired
    SubjectService subjectService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    TeacherService teacherService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CreditClassService creditClassService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AccountService accountService;

    @Autowired
    ClassRegistrationService classRegistrationService;

    @Autowired
    TimelineService timelineService;

    @Autowired
    FolderService folderService;

    @ApiOperation(value="Quản lý thêm lớp tín chỉ #23")
    @PostMapping("/create-new-class")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('TEACHER')")
    public ResponseEntity<?> createNewCreditClass(@Valid @RequestBody CreditClassRequest request){
        CreditClass creditClass = new CreditClass();
        creditClass.setDepartment(departmentService.getById(request.getDepartmentId()));
        creditClass.setSubject(subjectService.getById(request.getSubjectId()));
        try{
            creditClass.setStartTime(Timestamp.valueOf(request.getStartTime()));
            creditClass.setEndTime(Timestamp.valueOf(request.getEndTime()));
        }catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        creditClass.setSchoolYear(request.getSchoolYear());
        creditClass.setJoinedPassword(BCrypt.hashpw(request.getJoinedPassword().trim(),BCrypt.gensalt(12)));
        creditClass.setStatus(request.getStatus());
        Set<Teacher> teachers = new HashSet<>();

        boolean inValidTeacherId = false ;
        ///add list teacher for credit class
        for(String id: request.getTeacherId()){
            try{
                teachers.add(teacherService.getTeacherById(id));
            }catch (NotFoundException e){
                return ResponseEntity.notFound().build();
            }
        }
        creditClass.setTeachers(teachers);

        CreditClass creditClassRes = creditClassService.createNewCreditClassOrUpdate(creditClass);
        CreditClassResponse response = new CreditClassResponse();
        response.setCreditClassId(creditClassRes.getCreditClassId());
        response.setEndTime(request.getEndTime());
        response.setStartTime(request.getStartTime());
        response.setSchoolYear(request.getSchoolYear());
        response.setStatus(request.getStatus());
        response.setJoinedPassword(request.getJoinedPassword());
        response.setDepartmentId(request.getDepartmentId());
        response.setSubjectId(request.getSubjectId());
        response.setTeacherId(request.getTeacherId());
        return ResponseEntity.ok(response);
    }
    @ApiOperation(value="Lấy thông tin lớp tín chỉ cho việc cập nhật #87")
    @GetMapping("/get-credit-class-for-update")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('TEACHER')")
    public ResponseEntity<?> getCreditClassForUpdate(@RequestParam("creditclass-id") long creditClassId){
        CreditClass creditClass ;

        try{
            creditClass = creditClassService.getCreditClassById(creditClassId);
        }catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

        CreditClassResponse response = new CreditClassResponse();
        response.setCreditClassId(creditClass.getCreditClassId());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:sss");
        response.setEndTime(sdf.format(creditClass.getEndTime()));
        response.setStartTime(sdf.format(creditClass.getStartTime()));
        response.setSchoolYear(creditClass.getSchoolYear());
        response.setStatus(creditClass.getStatus());
        response.setJoinedPassword(creditClass.getJoinedPassword());
        response.setDepartmentId(creditClass.getDepartment().getDepartmentId());
        response.setSubjectId(creditClass.getSubject().getSubjectId());
        List<String> teacherIds = new ArrayList<>();
        for(Teacher t: creditClass.getTeachers()){
            teacherIds.add(t.getTeacherId());
        }
        response.setTeacherId(teacherIds);
        return ResponseEntity.ok(response);
    }
    @ApiOperation(value="Quản lý cập nhật thông tin lớp tín chỉ #24")
    @PutMapping("/update-credit-class")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('TEACHER')")
    public ResponseEntity<?> creditClass(@Valid @RequestBody CreditClassRequest request, @RequestParam(name = "credit-class-id") long creditClassId){
        CreditClass creditClass;
        try{
             creditClass = creditClassService.getCreditClassById(creditClassId);
            creditClass.setDepartment(departmentService.getById(request.getDepartmentId()));
            creditClass.setSubject(subjectService.getById(request.getSubjectId()));
        }catch(NotFoundException e){
            return ResponseEntity.notFound().build();
        }

        try{
            creditClass.setStartTime(Timestamp.valueOf(request.getStartTime()));
            creditClass.setEndTime(Timestamp.valueOf(request.getEndTime()));
        }catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        creditClass.setSchoolYear(request.getSchoolYear());
        creditClass.setJoinedPassword(request.getJoinedPassword());
        creditClass.setStatus(request.getStatus());
        Set<Teacher> teachers = new HashSet<>();

        ///add list teacher for credit class
        for(String id: request.getTeacherId()){
            try{
                teachers.add(teacherService.getTeacherById(id));
            }catch (NotFoundException e){
                return ResponseEntity.notFound().build();
            }
        }
        creditClass.setTeachers(teachers);

        CreditClass creditClassRes = creditClassService.createNewCreditClassOrUpdate(creditClass);//update
        CreditClassResponse response = new CreditClassResponse();
        response.setCreditClassId(creditClassRes.getCreditClassId());
        response.setEndTime(request.getEndTime());
        response.setStartTime(request.getStartTime());
        response.setSchoolYear(request.getSchoolYear());
        response.setStatus(request.getStatus());
        response.setJoinedPassword(request.getJoinedPassword());
        response.setDepartmentId(request.getDepartmentId());
        response.setSubjectId(request.getSubjectId());
        response.setTeacherId(request.getTeacherId());
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value="Hủy bỏ lớp tín chỉ #25 (chỉnh trạng thái của lớp tín chỉ từ '1' thành '0')")
    @PutMapping("/cancel-credit-class")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('TEACHER')")
    public ResponseEntity<?> cancelCreditClass(@RequestParam(name = "credit-class-id") long creditClassId){
        CreditClass creditClass;
        try{
            creditClass = creditClassService.getCreditClassById(creditClassId);
        }catch(NotFoundException e){
            return ResponseEntity.notFound().build();
        }
        creditClass.setStatus(0);

        CreditClass creditClassRes = creditClassService.createNewCreditClassOrUpdate(creditClass);//update
        CreditClassResponse response = new CreditClassResponse();

        response.setCreditClassId(creditClassRes.getCreditClassId());
        response.setStatus(creditClassRes.getStatus());
        //chỉ trả về mã lớp và trạng thái, không chứa các thông tin khác.
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value="Thêm sinh viên vào lớp tín chỉ #43")
    @PostMapping("/add-student-to-credit-class")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('TEACHER')")
    public ResponseEntity<?> addStudentToCreditClass(@RequestParam(name = "credit-class-id") long creditClassId, @RequestBody ListStudentIDDTO listStudentID, HttpServletRequest request){
        CreditClass creditClass;
        try{
            creditClass = creditClassService.getCreditClassById(creditClassId);
        }catch(NotFoundException e){
            return ResponseEntity.notFound().build();
        }
        java.lang.String username = jwtUtils.getUserNameFromJwtToken(request.getHeader("Authorization").substring(7));
        Account account = accountService.getAccountByUsername(username);
        Teacher teacher = null;
        if(isTeacherAndNotModerator(account)){
             teacher = teacherService.getByUserInfo(account.getUserInfo());
            if(!teacher.getCreditClasses().contains(creditClass)) return ResponseEntity.badRequest().body("You do not have permission!");
        }
        //tìm kiếm sinh viên dựa trên danh sách mã sinh viên
        try{
            classRegistrationService.addStudentToCreditClass(creditClass,listStudentID,teacher);
        }catch (NotFoundException e){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok("Add all students to credit class!");
    }
    @ApiOperation(value="Xóa sinh viên vào lớp tín chỉ #44")
    @PutMapping("/remove-student-from-credit-class")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('TEACHER')")
    public ResponseEntity<?> removeStudentToCreditClass(@RequestParam(name = "credit-class-id") long creditClassId, @RequestBody ListStudentIDDTO listStudentID, HttpServletRequest request){
        CreditClass creditClass;
        try{
            creditClass = creditClassService.getCreditClassById(creditClassId);
        }catch(NotFoundException e){
            return ResponseEntity.notFound().build();
        }
        java.lang.String username = jwtUtils.getUserNameFromJwtToken(request.getHeader("Authorization").substring(7));
        Account account = accountService.getAccountByUsername(username);
        Teacher teacher = null;

        //nếu là giảng viên(không phải là moderator ) thì phải dạy lớp đó mới được quyền xóa
        if(isTeacherAndNotModerator(account)){
            teacher = teacherService.getByUserInfo(account.getUserInfo());
            if(!teacher.getCreditClasses().contains(creditClass)) return ResponseEntity.badRequest().body("You do not have permission!");
        }

        try{
            classRegistrationService.removeStudentFromCreditClass(creditClass,listStudentID,teacher);
        }catch (NotFoundException e){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok("Removed students to credit class!");
    }
    @ApiOperation(value="Lấy thông tin timeline lớp tín chỉ #88")
    @GetMapping("/get-credit-class-time-line")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('TEACHER')")
    public ResponseEntity<?> getCreditClassTimeline(@RequestParam("creditclass-id") long creditClassId){
        CreditClass creditClass ;

        try{
            creditClass = creditClassService.getCreditClassById(creditClassId);
        }catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

        List<TimelineDTO> dtos = new ArrayList<>();

        timelineService.getByCreditClass(creditClass).forEach(t -> {
            TimelineDTO dto = new TimelineDTO();
            dto.setCreditClass(t.getCreditClass().getCreditClassId());
            dto.setSubjectName(t.getCreditClass().getSubject().getSubjectName());

            if (t.getCreditClass().getStartTime().getMonth() < 5) dto.setSemester(2);
            else if (t.getCreditClass().getStartTime().getMonth() < 8) dto.setSemester(3);
            else dto.setSemester(1);

            dto.setSchoolYear(t.getCreditClass().getSchoolYear());
            dto.setStartTime(t.getCreditClass().getStartTime().toString());
            dto.setEndTime(t.getCreditClass().getEndTime().toString());
            dto.setDayOfWeek(t.getTimelineId().getDayOfWeek());
            dto.setRoom(t.getRoom().getRoomName());
            dto.setStartLesson(t.getTimelineId().getStartLesson());
            dto.setEndLesson(t.getEndLesson());
            dtos.add(dto);
        });
        return ResponseEntity.ok(dtos);
    }
    @ApiOperation(value="Lấy thông tin thành viên của tất cả lớp tín chỉ #89")
    @GetMapping(value = {"/get-credit-class-total","/get-credit-class-total/{pageNo}"})
    @PreAuthorize("hasRole('MODERATOR') or hasRole('TEACHER')")
    public ResponseEntity<CreditClassPageForUserTotal> getCreditClassTotal(@PathVariable(required = false) Optional<Integer> pageNo){
        int pageNoOffical = 1;
        if (pageNo.isPresent()){
            if(pageNo.get()<=0){
                return ResponseEntity.badRequest().build();
            }
            pageNoOffical = pageNo.get();
        }
        String sortDir = "desc";
        String sortField = "creditClassId";
        int pageSize = 10;
        Page<CreditClass> creditClasses = creditClassService.pageOfTopTenActive(pageNoOffical, pageSize, sortField, sortDir);
        CreditClassPageForUserTotal creditClassPageForUserTotal = new CreditClassPageForUserTotal();
        creditClassPageForUserTotal.setTotalPage(creditClasses.getTotalPages());
        creditClassPageForUserTotal.setCreditClassDTOS(convertToDTO(creditClasses.getContent()));
        return ResponseEntity.ok(creditClassPageForUserTotal);
    }
    @ApiOperation(value="Lấy thông tin tất cả lớp tín chỉ cùng số bài post #90")
    @GetMapping(value = {"/get-credit-class-total-post","/get-credit-class-total-post/{pageNo}"})
    @PreAuthorize("hasRole('MODERATOR') or hasRole('TEACHER')")
    public ResponseEntity<CreditClassPageForUserPost> getCreditClassTotalWithPost(@PathVariable(required = false) Optional<Integer> pageNo){
        int pageNoOffical = 1;
        if (pageNo.isPresent()){
            if(pageNo.get()<=0){
                return ResponseEntity.badRequest().build();
            }
            pageNoOffical = pageNo.get();
        }
        String sortDir = "desc";
        String sortField = "creditClassId";
        int pageSize = 10;
        Page<CreditClass> creditClasses = creditClassService.pageOfTopTenActive(pageNoOffical, pageSize, sortField, sortDir);
        CreditClassPageForUserPost creditClassPageForUserPost = new CreditClassPageForUserPost();
        creditClassPageForUserPost.setTotalPage(creditClasses.getTotalPages());
        creditClassPageForUserPost.setCreditClassDTOS(convertToDTOWithPost(creditClasses.getContent()));
        return ResponseEntity.ok(creditClassPageForUserPost);
    }
    @ApiOperation(value="Lấy thông tin tất cả lớp tín chỉ cùng số tài liệu #91")
    @GetMapping(value = {"/get-credit-class-total-document","/get-credit-class-total-document/{pageNo}"})
    @PreAuthorize("hasRole('MODERATOR') or hasRole('TEACHER')")
    public ResponseEntity<CreditClassPageForUserDocument> getCreditClassTotalWithDocument(@PathVariable(required = false) Optional<Integer> pageNo){
        int pageNoOffical = 1;
        if (pageNo.isPresent()){
            if(pageNo.get()<=0){
                return ResponseEntity.badRequest().build();
            }
            pageNoOffical = pageNo.get();
        }
        String sortDir = "desc";
        String sortField = "creditClassId";
        int pageSize = 10;
        Page<CreditClass> creditClasses = creditClassService.pageOfTopTenActive(pageNoOffical, pageSize, sortField, sortDir);
        CreditClassPageForUserDocument creditClassPageForUserDocument = new CreditClassPageForUserDocument();
        creditClassPageForUserDocument.setTotalPage(creditClasses.getTotalPages());
        creditClassPageForUserDocument.setCreditClassDTOS(convertToDTOWithDocument(creditClasses.getContent()));
        return ResponseEntity.ok(creditClassPageForUserDocument);
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
    private List<CreditClassDTOwithQuantitty> convertToDTO(List<CreditClass> creditClasses) {
        List<CreditClassDTOwithQuantitty> creditClassDTOS = new ArrayList<>();
        creditClasses.forEach(creditClass -> {
            CreditClassDTOwithQuantitty dto = new CreditClassDTOwithQuantitty();
            dto.setCreditClassId(creditClass.getCreditClassId());
            dto.setSubjectName(creditClass.getSubject().getSubjectName());
            dto.setDepartmentName(creditClass.getDepartment().getDepartmentName());
            dto.setSchoolYear(creditClass.getSchoolYear());
            dto.setTotalTeacher(creditClass.getTeachers().size());
            dto.setTotalStudent(classRegistrationService.getListUserInfosByCreditClass(creditClass).size());
            if (creditClass.getStartTime().getMonth() < 5) dto.setSemester(2);
            else if (creditClass.getStartTime().getMonth() < 8) dto.setSemester(3);
            else dto.setSemester(1);
            creditClassDTOS.add(dto);
        });
        return creditClassDTOS;
    }
    private List<CreditClassDTOWithPostInfo> convertToDTOWithPost(List<CreditClass> creditClasses) {
        List<CreditClassDTOWithPostInfo> creditClassDTOS = new ArrayList<>();
        creditClasses.forEach(creditClass -> {
            CreditClassDTOWithPostInfo dto = new CreditClassDTOWithPostInfo();
            dto.setCreditClassId(creditClass.getCreditClassId());
            dto.setSubjectName(creditClass.getSubject().getSubjectName());
            dto.setDepartmentName(creditClass.getDepartment().getDepartmentName());
            dto.setSchoolYear(creditClass.getSchoolYear());
            dto.setTotalPost(creditClass.getPosts().size());

            int totalComment = 0;
            for(Post post: creditClass.getPosts()){
                totalComment+=post.getComments().size();
            }
            dto.setTotalComment(totalComment);

            if (creditClass.getStartTime().getMonth() < 5) dto.setSemester(2);
            else if (creditClass.getStartTime().getMonth() < 8) dto.setSemester(3);
            else dto.setSemester(1);
            creditClassDTOS.add(dto);
        });
        return creditClassDTOS;
    }
    private List<CreditClassDTOWithDocumentInfo> convertToDTOWithDocument(List<CreditClass> creditClasses) {
        List<CreditClassDTOWithDocumentInfo> creditClassDTOS = new ArrayList<>();
        creditClasses.forEach(creditClass -> {
            CreditClassDTOWithDocumentInfo dto = new CreditClassDTOWithDocumentInfo();
            dto.setCreditClassId(creditClass.getCreditClassId());
            dto.setSubjectName(creditClass.getSubject().getSubjectName());
            dto.setDepartmentName(creditClass.getDepartment().getDepartmentName());
            dto.setSchoolYear(creditClass.getSchoolYear());
            dto.setTotalFolder(creditClass.getFolders().size());
            int totalDocument = 0;
            for(Folder f: creditClass.getFolders()){
                totalDocument+= f.getDocuments().size();
            }
            dto.setTotalDocument(totalDocument);

            if (creditClass.getStartTime().getMonth() < 5) dto.setSemester(2);
            else if (creditClass.getStartTime().getMonth() < 8) dto.setSemester(3);
            else dto.setSemester(1);
            creditClassDTOS.add(dto);
        });
        return creditClassDTOS;
    }
}
