package com.ptit.Elearning.Controller;

import com.ptit.Elearning.DTO.*;
import com.ptit.Elearning.DTO.CreditclassDTOpk.CreditClassDTO;
import com.ptit.Elearning.DTO.DocumentDTOpk.DocumentResponseDTODetail;
import com.ptit.Elearning.DTO.PostDTO.PostDTO;
import com.ptit.Elearning.DTO.StudentDTOpk.StudentJoinClassRequestDTO;
import com.ptit.Elearning.DTO.TimelineDTOpk.TimelineDTO;
import com.ptit.Elearning.Entity.*;
import com.ptit.Elearning.Exception.NotFoundException;
import com.ptit.Elearning.Payloads.Request.Security.Jwt.JwtUtils;
import com.ptit.Elearning.Service.*;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AccountService accountService;


    @Autowired
    PostService postService;

    @Autowired
    CreditClassService creditClassService;

    @Autowired
    TimelineService timelineService;

    @Autowired
    SubmitService submitService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ClassRegistrationService classRegistrationService;

    @Autowired
    TeacherService teacherService;

    @Autowired
    StudentService studentService;


    @ApiOperation(value = "Lấy thông tin người dùng #8")
    @GetMapping("/get-user-info")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('TEACHER')")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer")) {
            java.lang.String username = jwtUtils.getUserNameFromJwtToken(token.substring(7));
            Account account = accountService.getAccountByUsername(username);
            UserInfoDTO userInfoDTO = convertToUserDTO(account.getUserInfo());
            userInfoDTO.setRoles(account.getRoles().stream().map(role -> role.getRoleName().name()).collect(Collectors.toList()));
            return ResponseEntity.ok(userInfoDTO);
        }
        return new ResponseEntity<>("Failed to get User's infomation", HttpStatus.BAD_REQUEST);
    }



    @ApiOperation(value = "Lấy 5 thông báo gần nhất của người dùng #5")
    @GetMapping(value = {"/top-five-post-currently"})
    public ResponseEntity<List<PostDTO>> getTopFivePostCurrently(HttpServletRequest request) {

        String username = jwtUtils.getUserNameFromJwtToken(request.getHeader("Authorization").substring(7));
        Account account = accountService.getAccountByUsername(username);
        UserInfo user = account.getUserInfo();
        List<ClassRegistration> registrations = classRegistrationService.getListClassByUserInfo(user);
        List<CreditClass> creditClasses = registrations.stream().filter(r -> r.getStatus() == 1).map(r -> r.getCreditClass()).filter(c -> c.getEndTime().after(new Date(System.currentTimeMillis()))).collect(Collectors.toList());
        List<Post> posts = creditClasses.stream().flatMap(this::getPost).collect(Collectors.toList());
        Collections.sort(posts, new SortPostById());
        int endIndex = posts.size() >= 5 ? 5 : posts.size();
        return ResponseEntity.ok(convertToPostDTO(posts.subList(0, endIndex)));

    }

    @ApiOperation(value = "Lấy thời khóa biểu của sinh viên #6")
    @GetMapping("/timetable")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TimelineDTO>> getUserTimetable(HttpServletRequest request) {
        String username = jwtUtils.getUserNameFromJwtToken(request.getHeader("Authorization").substring(7));
        Account account = accountService.getAccountByUsername(username);
        List<CreditClass> creditClasses = classRegistrationService.getListClassByUserInfo(account.getUserInfo()).stream().map(c -> c.getCreditClass()).collect(Collectors.toList());
        List<TimelineDTO> dtos = new ArrayList<>();

        creditClasses.stream().filter(c -> c.getEndTime().after(new Date(System.currentTimeMillis()))).flatMap(this::getTimeLineByCreditClass).forEach(t -> {
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
    @ApiOperation(value = "Lấy thời khóa biểu của sinh viên theo tham số thời gian với định dạng MM/dd/yyyy #6")
    @GetMapping("/timetable-by-time")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TimelineDTO>> getUserTimetableByTime(HttpServletRequest request, @RequestParam("date") java.util.Date date) {
        String username = jwtUtils.getUserNameFromJwtToken(request.getHeader("Authorization").substring(7));
        Account account = accountService.getAccountByUsername(username);
        //lấy danh sách lớp học của sinh viên đăng ký:
        List<CreditClass> creditClasses = classRegistrationService.getListClassByUserInfo(account.getUserInfo()).stream().map(c -> c.getCreditClass()).collect(Collectors.toList());
        List<TimelineDTO> dtos = new ArrayList<>();
        java.util.Date firstDay = firstDayOfWeek(date);
        java.util.Date lastDay = lastDayOfWeek(date);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        creditClasses.stream().filter(c ->((c.getEndTime().after(firstDay) || sdf.format(c.getEndTime()).equals(sdf.format(firstDay)) )
                        && (c.getStartTime().before(lastDay) || sdf.format(c.getStartTime()).equals(sdf.format(lastDay))) ))
                .flatMap(this::getTimeLineByCreditClass).forEach(t -> {
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
        Comparator<TimelineDTO> compare1 = Comparator.comparing(TimelineDTO::getDayOfWeek);
        Comparator<TimelineDTO> compare2 = Comparator.comparing(TimelineDTO::getStartLesson);
        Comparator<TimelineDTO> compare3 = compare1.thenComparing(compare2);

        return ResponseEntity.ok(dtos.stream().sorted(compare3).collect(Collectors.toList()));
    }
    @ApiOperation(value = "Lấy thời gian biểu của giảng viên theo tham số thời gian với định dạng MM/dd/yyyy #80")
    @GetMapping("/timetable-by-time-teacher")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<TimelineDTO>> getTeacherTimetableByTime(HttpServletRequest request, @RequestParam("date") java.util.Date date) {
        String username = jwtUtils.getUserNameFromJwtToken(request.getHeader("Authorization").substring(7));
        Account account = accountService.getAccountByUsername(username);
        //lấy danh sách lớp học của giảng viên dạy

        Set<CreditClass> creditClassesSet =teacherService.getByUserInfo(account.getUserInfo()).getCreditClasses();
        List<CreditClass> creditClasses = new ArrayList<>(creditClassesSet);

        List<TimelineDTO> dtos = new ArrayList<>();
        java.util.Date firstDay = firstDayOfWeek(date);
        java.util.Date lastDay = lastDayOfWeek(date);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");


        creditClasses.stream().filter(c ->((c.getEndTime().after(firstDay) || sdf.format(c.getEndTime()).equals(sdf.format(firstDay)) )
                        && (c.getStartTime().before(lastDay) || sdf.format(c.getStartTime()).equals(sdf.format(lastDay))) ))
                .flatMap(this::getTimeLineByCreditClass).forEach(t -> {
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
        Comparator<TimelineDTO> compare1 = Comparator.comparing(TimelineDTO::getDayOfWeek);
        Comparator<TimelineDTO> compare2 = Comparator.comparing(TimelineDTO::getStartLesson);
        Comparator<TimelineDTO> compare3 = compare1.thenComparing(compare2);

        return ResponseEntity.ok(dtos.stream().sorted(compare3).collect(Collectors.toList()));
    }

    @ApiOperation(value = "Lấy thời gian biểu của giảng viên theo tham số thời gian với định dạng MM/dd/yyyy (quyền Moderator) #81")
    @GetMapping("/timetable-by-time-admin")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> getTeacherTimetableByTimeByMod(HttpServletRequest request, @RequestParam("date") java.util.Date date,@RequestParam("teacher-id") String teacherId) {
        String username = jwtUtils.getUserNameFromJwtToken(request.getHeader("Authorization").substring(7));
        Account account = accountService.getAccountByUsername(username);
        Teacher teacher;

        //lấy danh sách lớp học của giảng viên dạy
        try{
            teacher = teacherService.getTeacherById(teacherId);
        }catch (NotFoundException e){

            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        Set<CreditClass> creditClassesSet =teacher.getCreditClasses();
        List<CreditClass> creditClasses = new ArrayList<>(creditClassesSet);

        List<TimelineDTO> dtos = new ArrayList<>();
        java.util.Date firstDay = firstDayOfWeek(date);
        java.util.Date lastDay = lastDayOfWeek(date);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");


        creditClasses.stream().filter(c ->((c.getEndTime().after(firstDay) || sdf.format(c.getEndTime()).equals(sdf.format(firstDay)) )
                        && (c.getStartTime().before(lastDay) || sdf.format(c.getStartTime()).equals(sdf.format(lastDay))) ))
                .flatMap(this::getTimeLineByCreditClass).forEach(t -> {
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
        Comparator<TimelineDTO> compare1 = Comparator.comparing(TimelineDTO::getDayOfWeek);
        Comparator<TimelineDTO> compare2 = Comparator.comparing(TimelineDTO::getStartLesson);
        Comparator<TimelineDTO> compare3 = compare1.thenComparing(compare2);

        return ResponseEntity.ok(dtos.stream().sorted(compare3).collect(Collectors.toList()));
    }
    @ApiOperation(value = "Danh sách các khóa học của sinh viên trong học kỳ hiện tại #10")
    @GetMapping("/registration")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<CreditClassDTO>> getUserRegistration(HttpServletRequest request) {
        String username = jwtUtils.getUserNameFromJwtToken(request.getHeader("Authorization").substring(7));
        Account account = accountService.getAccountByUsername(username);
        List<CreditClass> creditClasses = classRegistrationService.getListClassByUserInfo(account.getUserInfo()).stream().map(c -> c.getCreditClass()).filter(c -> c.getEndTime().after(new Date(System.currentTimeMillis()))).collect(Collectors.toList());

        return ResponseEntity.ok(convertToCreditClassDTO(creditClasses));
    }

    @ApiOperation(value = "Cập nhật mật khẩu mới cho người dùng #29")
    @PutMapping("/update-new-password")
    public ResponseEntity<?> updatePassword(HttpServletRequest request, @RequestBody NewPasswordDTO newPassword) {
        if (newPassword.getPassword().trim().length() < 6)
            return ResponseEntity.badRequest().body("The length of the password must be least at 6 charaters");
        Account account = accountService.getAccountByUsername(jwtUtils.getUserNameFromJwtToken(request.getHeader("Authorization").substring(7)));
        account.setPassword(encoder.encode(newPassword.getPassword().trim()));
        accountService.saveAnExistingAccount(account);
        return ResponseEntity.ok().body("Update password successfully!");
    }

    @ApiOperation(value = "Thông tin nộp bài của sinh viên #13")
    @GetMapping("/submit-info")
    public ResponseEntity<?> inforSubmit(@RequestParam(name = "excercise-id") Long excerciseId, HttpServletRequest request) {
        Account account = accountService.getAccountByUsername(jwtUtils.getUserNameFromJwtToken(request.getHeader("Authorization").substring(7)));
        try {
            Submit submit = submitService.getSubmitWithExcerciseIdAndUserId(excerciseId, account.getUserInfo().getUserId());
            return ResponseEntity.ok(convertToSubmitDTO(submit));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation(value = "Sinh viên tham gia lớp học #45")
    @PostMapping("/join-class")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> joinClass(@RequestBody StudentJoinClassRequestDTO requestModelDTO, HttpServletRequest request) {

        //check class exist:
        CreditClass creditClass;
        try {
            creditClass = creditClassService.getCreditClassById(requestModelDTO.getCreditClassId());
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }

        Account account = accountService.getAccountByUsername(jwtUtils.getUserNameFromJwtToken(request.getHeader("Authorization").substring(7)));

        //check join result
        boolean checkJoined = classRegistrationService.checkStudentJoinedClass(account.getUserInfo().getUserId(), requestModelDTO.getCreditClassId());

        //joined before: return unprocessable
        if (checkJoined == true) {
            return ResponseEntity.unprocessableEntity().body("You joined this class before");
        }

        //check join result: password must be correct.
        boolean checkJoinResult = classRegistrationService.joinToCreditClass(creditClass, account.getUserInfo(), requestModelDTO.getJoinCode());

        if (checkJoinResult == false) {//wrong password
            return ResponseEntity.badRequest().body("Wrong password");
        }
        return ResponseEntity.ok("Joined successfully");
    }

    @ApiOperation(value = "Kiểm tra sinh viên đã vào lớp hay chưa #72")
    @PostMapping("/check-joined")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> checkJoined(@RequestParam("creditclass-id") long creditClassId, HttpServletRequest request) {

        //check class exist:
        CreditClass creditClass;
        try {
            creditClass = creditClassService.getCreditClassById(creditClassId);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }

        Account account = accountService.getAccountByUsername(jwtUtils.getUserNameFromJwtToken(request.getHeader("Authorization").substring(7)));

        //check join result
        boolean checkJoined = classRegistrationService.checkStudentJoinedClass(account.getUserInfo().getUserId(), creditClassId);

        //joined before: return unprocessable
        if (checkJoined == true) {
            return ResponseEntity.ok("You have Joined ");
        }
        return ResponseEntity.unprocessableEntity().body("You have not joined this class before");

    }

    private UserInfoDTO convertToUserDTO(UserInfo userInfo) {


        UserInfoDTO userInfoDTO = modelMapper.map(userInfo, UserInfoDTO.class);
        if(userInfo.getAvatar()!=null){
            userInfoDTO.setAvatar((ServletUriComponentsBuilder.fromCurrentContextPath().path("api/avatar/dowload/")
                    .path(userInfo.getAvatar().getAvatarId().toString())
                    .toUriString()));
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        if(userInfo.getDateOfBirth()!=null)
            userInfoDTO.setDateOfBirth(simpleDateFormat.format(userInfo.getDateOfBirth()));

        try {
            Teacher teacher = teacherService.getByUserInfo(userInfo);

            userInfoDTO.setUserCode(teacher.getTeacherId());
            userInfoDTO.setUserClass(teacher.getDepartment().getDepartmentName());

        } catch (NotFoundException e) {
            try{
                Student student = studentService.getByUserInfo(userInfo);
                userInfoDTO.setUserCode(student.getStudentCode());
                userInfoDTO.setUserClass(student.getClassOf().getClassId());
            }catch (NotFoundException e1){
                userInfoDTO.setUserCode("ADMIN");
                userInfoDTO.setUserClass("ADMIN");
            }
        }


        return userInfoDTO;
    }

    private List<PostDTO> convertToPostDTO(List<Post> posts) {
        List<PostDTO> dtos = new ArrayList<>();
        posts.forEach(p -> {
            PostDTO dto = modelMapper.map(p, PostDTO.class);
            dto.setFullname(p.getUserInfo().getFullname());

            if(p.getUserInfo().getAvatar()!=null){
                dto.setAvartarPublisher(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/avatar/dowload/")
                        .path(p.getUserInfo().getAvatar().getAvatarId().toString())
                        .toUriString());
            }


            dto.setSubjectName(p.getCreditClass().getSubject().getSubjectName());
            dto.setPostedTime(p.getCreatedAt().toString());

            dtos.add(dto);
        });
        return dtos;
    }

    private List<CreditClassDTO> convertToCreditClassDTO(List<CreditClass> creditClasses) {
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

    public Stream<Timeline> getTimeLineByCreditClass(CreditClass creditClass) {
        return timelineService.getByCreditClass(creditClass).stream();
    }

    private ExcerciseDTODetailForSubmit convertToSubmitDTO(Submit submit) {
        ExcerciseDTODetailForSubmit dto = new ExcerciseDTODetailForSubmit();
        dto.setExcerciseId(submit.getExcercise().getExcerciseId());
        dto.setExcerciseTitle(submit.getExcercise().getTitle());
        dto.setExcerciseContent(submit.getExcercise().getExcerciseContent());
        dto.setStartTime(submit.getExcercise().getStartTime().toString());
        dto.setEndTime(submit.getExcercise().getEndTime().toString());
        dto.setSubmitTime(submit.getSubmitTime().toString());
        dto.setSubmitContent(submit.getSubmitContent());

        List<DocumentResponseDTODetail> documentDTOS = submit.getExcercise().getDocuments().stream().map(d -> modelMapper.map(d, DocumentResponseDTODetail.class)).collect(Collectors.toList());
        dto.setDocuments(documentDTOS);
        dto.setSubmitFile(modelMapper.map(submit.getDocument(), DocumentResponseDTODetail.class));
        return dto;
    }

    private Stream<Post> getPost(CreditClass creditClass) {
        return postService.getTopFivePostInCreditClass(creditClass).stream();

    }

    class SortPostById implements Comparator<Post> {
        @Override
        public int compare(Post o1, Post o2) {
            return (int) (o2.getPostId() - o1.getPostId());
        }
    }


    private java.util.Date firstDayOfWeek(java.util.Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        return calendar.getTime();
    }
    private java.util.Date lastDayOfWeek(java.util.Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, 7);
        return calendar.getTime();
    }
}
