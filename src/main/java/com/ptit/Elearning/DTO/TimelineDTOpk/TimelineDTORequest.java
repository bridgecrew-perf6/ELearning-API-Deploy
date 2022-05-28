package com.ptit.Elearning.DTO.TimelineDTOpk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TimelineDTORequest {
    private Long creditClassId;

    private int dayOfWeek;

    private int startLesson;
    private int endLesson;

    private int roomId;

}
