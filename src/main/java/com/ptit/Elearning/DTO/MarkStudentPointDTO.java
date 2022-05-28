package com.ptit.Elearning.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MarkStudentPointDTO {
    private Long excerciseId;

    private Long userId;

    @Max(10)
    @Min(0)
    private Long mark;
}
