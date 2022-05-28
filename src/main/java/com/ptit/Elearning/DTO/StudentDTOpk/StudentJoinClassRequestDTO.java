package com.ptit.Elearning.DTO.StudentDTOpk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentJoinClassRequestDTO {
    private Long creditClassId;
    private String joinCode;
}
