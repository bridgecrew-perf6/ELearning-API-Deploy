package com.ptit.Elearning.DTO.TimelineDTOpk;

import com.ptit.Elearning.Entity.TimelineId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateTimelineDTO {
    TimelineId timelineId;
    TimelineDTORequest timelineDTORequest;
}
