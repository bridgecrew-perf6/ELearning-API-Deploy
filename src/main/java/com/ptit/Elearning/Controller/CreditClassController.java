package com.ptit.Elearning.Controller;

import com.ptit.Elearning.Constant.ERole;
import com.ptit.Elearning.DTO.CreditclassDTOpk.CreditClassDTO;
import com.ptit.Elearning.DTO.CreditclassDTOpk.CreditClassDetailDTO;
import com.ptit.Elearning.DTO.CreditclassDTOpk.CreditClassListMemberDTO;
import com.ptit.Elearning.DTO.CreditclassDTOpk.CreditClassPageForUser;
import com.ptit.Elearning.DTO.DocumentDTOpk.DocumentDTO;
import com.ptit.Elearning.DTO.ExcerciseDTOpk.ExcerciseDTO;
import com.ptit.Elearning.DTO.FolderDTOpk.FolderDTOResponse;
import com.ptit.Elearning.DTO.PostDTO.PostDTO;
import com.ptit.Elearning.DTO.PostDTO.PostDTOWithComment;
import com.ptit.Elearning.DTO.StudentDTOpk.StudentDTO;
import com.ptit.Elearning.DTO.TeacherDTOpk.TeacherInfoDTO;
import com.ptit.Elearning.Entity.*;
import com.ptit.Elearning.Exception.NotFoundException;
import com.ptit.Elearning.Payloads.Request.Security.Jwt.JwtUtils;
import com.ptit.Elearning.Repository.RoleRepository;
import com.ptit.Elearning.Service.*;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/credit-class")
public class CreditClassController {

    @Autowired
    CreditClassService creditClassService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    ExcerciseService excerciseService;

    @Autowired
    ClassRegistrationService classRegistrationService;

    @Autowired
    StudentService studentService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AccountService accountService;

    @Autowired
    TeacherService teacherService ;

    @Autowired
    RoleRepository roleRepository ;
    @ApiOperation(value = "Lấy danh sách các lớp tín chỉ theo trang(size= 10) #4")
    @GetMapping(value = {"/{pageNo}", "/"})
    public ResponseEntity<List<CreditClassDTO>> getTopCreditClass(@PathVariable(required = false) Optional<Integer> pageNo) {
        int pageNoOffical = 1;
        if (pageNo.isPresent()){
            if(pageNo.get()<=0){
                return ResponseEntity.badRequest().build();
            }
            pageNoOffical = pageNo.get();
        }
        String sortDir = "desc";
        String sortField = "creditClassId";
        int pageSize = 8;
        List<CreditClass> creditClasses = creditClassService.pageOfTopTenActive(pageNoOffical, pageSize, sortField, sortDir).getContent();
        return ResponseEntity.ok(convertToDTO(creditClasses));
    }
    @ApiOperation(value = "Lấy danh sách các lớp tín chỉ theo trang(size= 10) #4 newly")
    @GetMapping(value = {"/all/{pageNo}", "/all/"})
    public ResponseEntity<CreditClassPageForUser> getCreditClassPage(@PathVariable(required = false) Optional<Integer> pageNo) {
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
        CreditClassPageForUser page = new CreditClassPageForUser();
        page.setTotalPage(creditClasses.getTotalPages());
        page.setCreditClassDTOS(convertToDTO(creditClasses.getContent()));
        return ResponseEntity.ok(page);
    }
    @ApiOperation(value="Lấy danh sách các lớp tỉn chỉ theo niên khóa-học kỳ-khoa #11")
    @GetMapping(value = {"/get-credit-class", "/get-credit-class/{page}"})
    public ResponseEntity<?> viewSchoolYearAndSemesterAndDepartment(@RequestParam(name = "schoolyear") String schoolYear, @RequestParam(name = "department_id") int departmentId, @RequestParam int semester, @PathVariable(required = false) Optional<Integer> page) {
        int pageNoOffical = 1;
        if (page.isPresent()){
            if(page.get()<=0){
                return ResponseEntity.badRequest().build();
            }
            pageNoOffical = page.get();
        }
        String sortDir = "desc";
        String sortField = "creditClassId";
        int pageSize = 10;

        List<CreditClass> creditClasses = null;
        try {
            creditClasses = creditClassService.pageOfCreditClassViaSchoolYearAndSemesterAndDepartment(pageNoOffical, pageSize, sortField, sortDir, schoolYear, semester, departmentService.getById(departmentId));
        } catch (IllegalAccessException e) {
            return ResponseEntity.badRequest().body("Semester is not valid. Must be from 1 to 3");
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body("Department is unavaiable");
        }
        return ResponseEntity.ok(convertToDTO(creditClasses));
    }

    @ApiOperation(value="Lấy danh sách các lớp tỉn chỉ theo niên khóa-học kỳ-khoa(có phân trang) #63")
    @GetMapping(value = {"/all/get-credit-class", "/all/get-credit-class/{page}"})
    public ResponseEntity<?> getCreditClassViaInfobasic(@RequestParam(name = "schoolyear") String schoolYear, @RequestParam(name = "department_id") int departmentId, @RequestParam int semester, @PathVariable(required = false) Optional<Integer> page) {
        int pageNoOffical = 1;
        if (page.isPresent()){
            if(page.get()<=0){
                return ResponseEntity.badRequest().build();
            }
            pageNoOffical = page.get();
        }
        String sortDir = "desc";
        String sortField = "credit_class_id";
        int pageSize = 10;

        Page<CreditClass> pageCreditClass;
        try {
            Department department = departmentService.getById(departmentId);
            pageCreditClass = creditClassService.pageOfCreditClassByBasicInfo(pageNoOffical, pageSize, sortField, sortDir, schoolYear, semester, departmentId);
        } catch (IllegalAccessException e) {
            return ResponseEntity.badRequest().body("Semester is not valid. Must be from 1 to 3");
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body("Department is unavaiable");
        }
        CreditClassPageForUser creditClassPageForUser = new CreditClassPageForUser();

        creditClassPageForUser.setTotalPage(pageCreditClass.getTotalPages());
        creditClassPageForUser.setCreditClassDTOS(convertToDTO(pageCreditClass.getContent()));

        return ResponseEntity.ok(creditClassPageForUser);
    }

    @ApiOperation(value="Thông tin chi tiết của lớp tin chỉ #14")
    @GetMapping("/creditclass-detail")
    public ResponseEntity<?> getById(@RequestParam(name = "creditclass_id") Long creditClassId) {
        CreditClass creditClass;
        try {
            creditClass = creditClassService.getCreditClassById(creditClassId);

        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
        return ResponseEntity.ok(convertToCreditClassDetail(creditClass));
    }
    @ApiOperation(value="Lấy các bài post trong lớp tín chỉ #88")
    @GetMapping("/creditclass-list-post")
    public ResponseEntity<?> getPostsInClass(@RequestParam(name = "creditclass_id") Long creditClassId) {
        CreditClass creditClass;
        try {
            creditClass = creditClassService.getCreditClassById(creditClassId);

        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
        return ResponseEntity.ok(convertToPostWithCommentQuantiy(creditClass));
    }

    @ApiOperation(value="Lấy tất cả thông tin của sinh viên, giảng viên trong lớp tín chỉ #15")
    @GetMapping("/creditclass-all-members")
    public ResponseEntity<?> allMembers(@RequestParam(name = "creditclass_id") Long creditClassId) {
        CreditClass creditClass;
        try {
            creditClass = creditClassService.getCreditClassById(creditClassId);
            List<Student> students = classRegistrationService.getListUserInfosByCreditClass(creditClass).stream().map(u->studentService.getByUserInfo(u)).collect(Collectors.toList());

            List<StudentDTO> studentDTOS = new ArrayList<>();
            students.forEach(s->{
                StudentDTO dto = new StudentDTO();
                dto.setStudentCode(s.getStudentCode());
                dto.setFullnanme(s.getUserInfo().getFullname());
                studentDTOS.add(dto);
            });
            List<TeacherInfoDTO> teacherInfos = creditClass.getTeachers().stream().map(t->t.getUserInfo()).map(this::convertToTeacherDTO).collect(Collectors.toList());

            CreditClassListMemberDTO dto = new CreditClassListMemberDTO();
            dto.setTeacherInfos(teacherInfos);
            dto.setStudents(studentDTOS);
            return ResponseEntity.ok(dto);

        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
    }
    @ApiOperation(value="Lấy tất cả thông tin của sinh viên, giảng viên trong lớp tín chỉ #15 newly")
    @GetMapping("/creditclass-all-members-active")
    public ResponseEntity<?> allMembersActive(@RequestParam(name = "creditclass_id") Long creditClassId) {
        CreditClass creditClass;
        try {
            creditClass = creditClassService.getCreditClassById(creditClassId);
            List<Student> students = classRegistrationService.getListUserInfosByCreditClassActive(creditClass).stream().map(u->studentService.getByUserInfo(u)).collect(Collectors.toList());
            List<StudentDTO> studentDTOS = new ArrayList<>();
            students.forEach(s->{
                StudentDTO dto = new StudentDTO();
                dto.setStudentCode(s.getStudentCode());
                dto.setFullnanme(s.getUserInfo().getFullname());
                studentDTOS.add(dto);
            });
            List<TeacherInfoDTO> teacherInfos = creditClass.getTeachers().stream().map(t->t.getUserInfo()).map(this::convertToTeacherDTO).collect(Collectors.toList());

            CreditClassListMemberDTO dto = new CreditClassListMemberDTO();
            dto.setTeacherInfos(teacherInfos);
            dto.setStudents(studentDTOS);
            return ResponseEntity.ok(dto);

        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
    }
    @ApiOperation(value="Lấy tổng số sinh viên của lớp tỉn chỉ #42")
    @GetMapping("/creditclass-total-student")
    @PreAuthorize("hasRole('TEACHER') or hasRole('MODERATOR')")
    public ResponseEntity<?> numberOfStudent(HttpServletRequest request, @RequestParam(name = "creditclass_id") Long creditClassId) {
        CreditClass creditClass;
        try {
            creditClass = creditClassService.getCreditClassById(creditClassId);
            java.lang.String username = jwtUtils.getUserNameFromJwtToken(request.getHeader("Authorization").substring(7));
            Account account = accountService.getAccountByUsername(username);
            if(isTeacherAndNotModerator(account)){
                Teacher teacher = teacherService.getByUserInfo(account.getUserInfo());
                if(!teacher.getCreditClasses().contains(creditClass)) return ResponseEntity.badRequest().body("You do not have permission!");
            }
            return ResponseEntity.ok(classRegistrationService.getListUserInfosByCreditClass(creditClass).size());
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
    }
    @ApiOperation(value="Lấy danh sách các lớp tỉn chỉ theo niên khóa-học kỳ-khoa-tên lớp học #64")
    @GetMapping(value = {"/get-credit-class/with-name", "/get-credit-class/with-name/{page}"})
    public ResponseEntity<?> getCreditClassViaInfobasicAndName(@RequestParam(name = "schoolyear") String schoolYear, @RequestParam(name = "department_id") int departmentId, @RequestParam int semester,@RequestParam String name, @PathVariable(required = false) Optional<Integer> page) {
        int pageNoOffical = 1;
        if (page.isPresent()){
            if(page.get()<=0){
                return ResponseEntity.badRequest().build();
            }
            pageNoOffical = page.get();
        }
        String sortDir = "desc";
        String sortField = "credit_class_id";
        int pageSize = 10;

        Page<CreditClass> pageCreditClass;
        try {
            Department department = departmentService.getById(departmentId);
            pageCreditClass = creditClassService.pageOfCreditClassByBasicInfoAndName(pageNoOffical, pageSize, sortField, sortDir, schoolYear, semester, departmentId,name.trim());
        } catch (IllegalAccessException e) {
            return ResponseEntity.badRequest().body("Semester is not valid. Must be from 1 to 3");
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body("Department is unavaiable");
        }
        CreditClassPageForUser creditClassPageForUser = new CreditClassPageForUser();

        creditClassPageForUser.setTotalPage(pageCreditClass.getTotalPages());
        creditClassPageForUser.setCreditClassDTOS(convertToDTO(pageCreditClass.getContent()));

        return ResponseEntity.ok(creditClassPageForUser);
    }

    @ApiOperation(value="Lấy danh sách các lớp tỉn chỉ theo tên lớp học #65")
    @GetMapping(value = {"/get-credit-class/name-only", "/get-credit-class/name-only/{page}"})
    public ResponseEntity<?> getCreditClassViaName(@RequestParam String name, @PathVariable(required = false) Optional<Integer> page) {
        int pageNoOffical = 1;
        if (page.isPresent()){
            if(page.get()<=0){
                return ResponseEntity.badRequest().build();
            }
            pageNoOffical = page.get();
        }
        String sortDir = "desc";
        String sortField = "credit_class_id";
        int pageSize = 10;

        Page<CreditClass> pageCreditClass;

        pageCreditClass = creditClassService.pageOfCreditClassByName(pageNoOffical, pageSize, sortField, sortDir,name.trim());

        CreditClassPageForUser creditClassPageForUser = new CreditClassPageForUser();

        creditClassPageForUser.setTotalPage(pageCreditClass.getTotalPages());

        creditClassPageForUser.setCreditClassDTOS(convertToDTO(pageCreditClass.getContent()));

        return ResponseEntity.ok(creditClassPageForUser);
    }

    private List<CreditClassDTO> convertToDTO(List<CreditClass> creditClasses) {
        List<CreditClassDTO> creditClassDTOS = new ArrayList<>();
        creditClasses.forEach(creditClass -> {
            CreditClassDTO dto = new CreditClassDTO();
            dto.setCreditClassId(creditClass.getCreditClassId());
            dto.setSubjectName(creditClass.getSubject().getSubjectName());
            dto.setTeachers(creditClass.getTeachers().stream().map(t -> t.getUserInfo().getFullname()).collect(Collectors.toList()));
            dto.setDepartmentName(creditClass.getDepartment().getDepartmentName());
            dto.setSchoolYear(creditClass.getSchoolYear());
            if (creditClass.getStartTime().getMonth() < 5) dto.setSemester(2);
            else if (creditClass.getStartTime().getMonth() < 8) dto.setSemester(3);
            else dto.setSemester(1);
            creditClassDTOS.add(dto);
        });
        return creditClassDTOS;
    }
    private CreditClassDetailDTO convertToCreditClassDetail(CreditClass creditClass){
        CreditClassDetailDTO dto = new CreditClassDetailDTO();

        dto.setCreditClassName(creditClass.getSubject().getSubjectName());
        dto.setStartTime(creditClass.getStartTime().toString());
        dto.setEndTime(creditClass.getEndTime().toString());
        dto.setDepartmentName(creditClass.getDepartment().getDepartmentName());

        List<TeacherInfoDTO> teacherInfos = creditClass.getTeachers().stream().map(t->t.getUserInfo()).map(this::convertToTeacherDTO).collect(Collectors.toList());
        dto.setTeacherInfos(teacherInfos);

//        dto.setFolders(modelMapper.map(creditClass.getFolders(),new TypeToken<List<FolderDTOResponse>>(){}.getType()));
        List<FolderDTOResponse> folderDTOResponses = convertToFolderDTO(creditClass.getFolders());
        Collections.sort(folderDTOResponses,new SortFolderById());
        dto.setFolders(folderDTOResponses);

        List<PostDTO> postDTOS = new ArrayList<>();
        List<Post> posts = creditClass.getPosts().stream().collect(Collectors.toList());
        Collections.sort(posts, new SortPostById());

        posts.forEach(p ->{
            if(p.getStatus()==1){
                PostDTO postDTO = modelMapper.map(p,PostDTO.class);
                postDTO.setFullname(p.getUserInfo().getFullname());
                if(p.getUserInfo().getAvatar()!=null){
                    postDTO.setAvartarPublisher(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/avatar/dowload/")
                            .path(p.getUserInfo().getAvatar().getAvatarId().toString())
                            .toUriString());
                }
                postDTO.setSubjectName(p.getCreditClass().getSubject().getSubjectName());
                postDTO.setPostedTime(p.getCreatedAt().toString());

                postDTOS.add(postDTO);
            }
        });

        dto.setListPost(postDTOS);
        dto.setExcercises(modelMapper.map(excerciseService.getByCreditClass(creditClass),new TypeToken<List<ExcerciseDTO>>(){}.getType()));

        return dto;
    }
    private  List<PostDTOWithComment>  convertToPostWithCommentQuantiy(CreditClass creditClass){

        List<PostDTOWithComment> postDTOS = new ArrayList<>();
        List<Post> posts = creditClass.getPosts().stream().collect(Collectors.toList());
        Collections.sort(posts, new SortPostById());

        posts.forEach(p ->{
            if(p.getStatus()==1){
                PostDTOWithComment postDTO = modelMapper.map(p,PostDTOWithComment.class);
                postDTO.setFullname(p.getUserInfo().getFullname());
                if(p.getUserInfo().getAvatar()!=null){
                    postDTO.setAvartarPublisher(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/avatar/dowload/")
                            .path(p.getUserInfo().getAvatar().getAvatarId().toString())
                            .toUriString());
                }
                postDTO.setSubjectName(p.getCreditClass().getSubject().getSubjectName());
                postDTO.setPostedTime(p.getCreatedAt().toString());
                postDTO.setQuantityComments(p.getComments().size());
                postDTOS.add(postDTO);
            }
        });

        return postDTOS;
    }
    private TeacherInfoDTO convertToTeacherDTO(UserInfo userInfo){
        return modelMapper.map(userInfo,TeacherInfoDTO.class);
    }
    class SortPostById implements Comparator<Post> {
        @Override
        public int compare(Post o1, Post o2) {
            return o2.getPostId().compareTo(o1.getPostId());
        }
    }
    class SortFolderById implements Comparator<FolderDTOResponse> {
        @Override
        public int compare(FolderDTOResponse o1, FolderDTOResponse o2) {
            return o2.getFolderId().compareTo(o1.getFolderId());
        }
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
    public List<FolderDTOResponse> convertToFolderDTO(Set<Folder> folders){
        List<FolderDTOResponse> dtos = new ArrayList<>();

        folders.forEach(f->{
            FolderDTOResponse response = new FolderDTOResponse();
            response.setFolderId(f.getFolderId());
            response.setFolderName(f.getFolderName());
            response.setDocuments(modelMapper.map(f.getDocuments(),new TypeToken<Set<DocumentDTO>>(){}.getType()));
            if(f.getParentsFolder()!=null){
                response.setParentsFolder(f.getParentsFolder().getFolderId());
            }
            response.setUpTime(f.getUpTime());
            dtos.add(response);
        });
        return dtos;
    }
}
