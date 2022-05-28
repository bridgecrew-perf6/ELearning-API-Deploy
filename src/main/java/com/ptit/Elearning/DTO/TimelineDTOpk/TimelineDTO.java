package com.ptit.Elearning.DTO.TimelineDTOpk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TimelineDTO {
    private Long creditClass;
    private String subjectName;
    private int semester;
    private String schoolYear;
    private String startTime;
    private String endTime;
    private int dayOfWeek;
    private String room;
    private int startLesson;
    private int endLesson;
}
