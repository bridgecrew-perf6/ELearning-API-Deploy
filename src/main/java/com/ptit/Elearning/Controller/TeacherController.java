package com.ptit.Elearning.Controller;

import com.ptit.Elearning.DTO.CreditclassDTOpk.CreditClassDTO;
import com.ptit.Elearning.DTO.TeacherDTOpk.TeacherDTOWithID;
import com.ptit.Elearning.DTO.TeacherDTOpk.TeacherInfoDTO;
import com.ptit.Elearning.DTO.TimelineDTOpk.TimelineDTO;
import com.ptit.Elearning.Entity.*;
import com.ptit.Elearning.Exception.NotFoundException;
import com.ptit.Elearning.Payloads.Request.Security.Jwt.JwtUtils;
import com.ptit.Elearning.Service.AccountService;
import com.ptit.Elearning.Service.TeacherService;
import com.ptit.Elearning.Service.TimelineService;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CrossOrigin(value ="*",maxAge = 3600)
@RestController
@RequestMapping("/api/teacher")
public class TeacherController {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    TeacherService teacherService;

    @Autowired
    TimelineService timelineService;

    @Autowired
    AccountService accountService;

    @Autowired
    ModelMapper modelMapper;

    @ApiOperation(value="Lấy thời khóa biểu các giảng viên #6")
    @GetMapping("/timetable")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<TimelineDTO>> timeTable(HttpServletRequest request){
        String username = request.getHeader("Authorization").substring(7);
        Account account = accountService.getAccountByUsername(jwtUtils.getUserNameFromJwtToken(username));
        Teacher teacher = teacherService.getByUserInfo(account.getUserInfo());
        List<TimelineDTO> dtos = new ArrayList<>();
        teacher.getCreditClasses().stream().filter(c-> c.getEndTime().after(new Date(System.currentTimeMillis()))).flatMap(this::getTimeLineByCreditClass).forEach(t->{
            TimelineDTO dto = new TimelineDTO();
            dto.setCreditClass(t.getCreditClass().getCreditClassId());
            dto.setSubjectName(t.getCreditClass().getSubject().getSubjectName());

            if(t.getCreditClass().getStartTime().getMonth()<5) dto.setSemester(2);
            else if(t.getCreditClass().getStartTime().getMonth()<8) dto.setSemester(3);
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

    @ApiOperation(value="Lấy các môn học của giảng viên trong học kỳ hiện tại#82")
    @GetMapping("/timetable-semester")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> getAllSubjectInThisSemester(HttpServletRequest request){


        String username = request.getHeader("Authorization").substring(7);
        Account account = accountService.getAccountByUsername(jwtUtils.getUserNameFromJwtToken(username));
        Teacher teacher = teacherService.getByUserInfo(account.getUserInfo());
        List<CreditClassDTO> dtos = new ArrayList<>();

        List<CreditClass> creditClasses = teacher.getCreditClasses().stream().filter(c -> c.getEndTime().after(new java.sql.Date(System.currentTimeMillis()))).collect(Collectors.toList());

        return ResponseEntity.ok(convertToCreditClassDTO(creditClasses));
    }
    @ApiOperation(value="Lấy các môn học của giảng viên trong học kỳ hiện tại(quyền moderator)#83 ")
    @GetMapping("/timetable-semester-admin")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> getAllSubjectInThisSemesterByAdmin(@RequestParam("teacher-id") String teacherId, @RequestParam("year") int year,@RequestParam("semester") int semester){

        if(semester<1 || semester>3){
            return ResponseEntity.badRequest().body("Semester must be from 1 to 3");
        }
        Teacher teacher;
        try{
             teacher = teacherService.getTeacherById(teacherId);
        }catch (NotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        List<CreditClassDTO> dtos = new ArrayList<>();


        //mặc định là học kỳ 1:
        int startMonth = 9;
        int endMonth = 12;

        //học kỳ 2
        if(semester==2){
            startMonth = 1;
            endMonth = 5;
        }
        //học kỳ 3:
        else if(semester==3){
            startMonth = 6;
            endMonth = 8;
        }
        final int startMonth0 = startMonth;
        final  int endMonth0 = endMonth;
        List<CreditClass> creditClasses = teacher.getCreditClasses().stream().filter(c-> (c.getEndTime().getYear()+1900==year && (c.getStartTime().getMonth()+1)>=startMonth0 && (c.getEndTime().getMonth()+1)<=endMonth0))
                .collect(Collectors.toList());

        return ResponseEntity.ok(convertToCreditClassDTO(creditClasses));
    }
    @ApiOperation(value="Lấy danh sách giảng viên (quyền moderator)#86 ")
    @GetMapping("/all")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<List<TeacherDTOWithID>> getALlTeachers(){
        List<Teacher> list = teacherService.getAllTeachers();
        List<TeacherDTOWithID> dtos = new ArrayList<>();
        list.forEach(t->{
            TeacherDTOWithID teacherDTOWithID = modelMapper.map(t.getUserInfo(),TeacherDTOWithID.class);
            teacherDTOWithID.setTeacherId(t.getTeacherId());
            dtos.add(teacherDTOWithID);
        });
        return ResponseEntity.ok(dtos);
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
    public Stream<Timeline> getTimeLineByCreditClass(CreditClass creditClass){
        return timelineService.getByCreditClass(creditClass).stream();
    }
    private TeacherInfoDTO convertToTeacherDTO(UserInfo userInfo){
        return modelMapper.map(userInfo,TeacherInfoDTO.class);
    }
}
