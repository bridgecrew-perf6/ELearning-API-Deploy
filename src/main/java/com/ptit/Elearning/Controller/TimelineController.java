package com.ptit.Elearning.Controller;

import com.ptit.Elearning.DTO.TimelineDTOpk.TimelineDTORequest;
import com.ptit.Elearning.DTO.TimelineDTOpk.UpdateTimelineDTO;
import com.ptit.Elearning.Entity.CreditClass;
import com.ptit.Elearning.Entity.Room;
import com.ptit.Elearning.Entity.Timeline;
import com.ptit.Elearning.Entity.TimelineId;
import com.ptit.Elearning.Exception.NotFoundException;
import com.ptit.Elearning.Service.CreditClassService;
import com.ptit.Elearning.Service.RoomService;
import com.ptit.Elearning.Service.TimelineService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(value = "*",maxAge = 3600)
@RestController
@RequestMapping("/api/timeline")
public class TimelineController {

    @Autowired
    RoomService roomService;

    @Autowired
    CreditClassService creditClassService;

    @Autowired
    TimelineService timelineService;

    @ApiOperation("Thêm thời gian biểu mới #57")
    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/create-new")
    public ResponseEntity<?> addNew(@RequestBody TimelineDTORequest request){
        try{
            CreditClass creditClass = creditClassService.getCreditClassById(request.getCreditClassId());
            Room room = roomService.getById(request.getRoomId());

            if(request.getStartLesson()<1 || request.getEndLesson()>10 || request.getStartLesson()>=request.getEndLesson() ){
                return new ResponseEntity<String>("Start lesson and End lesson is invalid!", HttpStatus.BAD_REQUEST);
            }
            if(request.getDayOfWeek()<0 || request.getDayOfWeek()>6){
                return new ResponseEntity<String>("Day of week is invalid", HttpStatus.BAD_REQUEST);
            }
            Timeline timeline = new Timeline();
            timeline.setTimelineId(new TimelineId(request.getCreditClassId(),request.getDayOfWeek(),request.getStartLesson(),request.getRoomId()));
            timeline.setEndLesson(request.getEndLesson());
            return ResponseEntity.ok(timelineService.createNewTimeline(timeline));
        }catch (NotFoundException e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation("Cập nhật thời gian biểu #58")
    @PreAuthorize("hasRole('MODERATOR')")
    @PutMapping("/update")
    public ResponseEntity<?> updateTimeline(@RequestBody UpdateTimelineDTO dto){

        try{
            Timeline timeline = timelineService.getById(dto.getTimelineId());

            CreditClass creditClass = creditClassService.getCreditClassById(dto.getTimelineDTORequest().getCreditClassId());
            Room room = roomService.getById(dto.getTimelineDTORequest().getRoomId());

            if(dto.getTimelineDTORequest().getStartLesson()<1 || dto.getTimelineDTORequest().getEndLesson()>10 || dto.getTimelineDTORequest().getStartLesson()>=dto.getTimelineDTORequest().getEndLesson() ){
                return new ResponseEntity<String>("Start lesson and End lesson is invalid!", HttpStatus.BAD_REQUEST);
            }
            if(dto.getTimelineDTORequest().getDayOfWeek()<0 || dto.getTimelineDTORequest().getDayOfWeek()>6){
                return new ResponseEntity<String>("Day of week is invalid", HttpStatus.BAD_REQUEST);
            }
            Timeline timelineUpdate = new Timeline();
            timelineUpdate.setTimelineId(new TimelineId(dto.getTimelineDTORequest().getCreditClassId(),dto.getTimelineDTORequest().getDayOfWeek(),dto.getTimelineDTORequest().getStartLesson(),dto.getTimelineDTORequest().getRoomId()));
            timelineUpdate.setEndLesson(dto.getTimelineDTORequest().getEndLesson());

            return ResponseEntity.ok(timelineService.updateById(timeline,timelineUpdate));
        }catch (NotFoundException e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
