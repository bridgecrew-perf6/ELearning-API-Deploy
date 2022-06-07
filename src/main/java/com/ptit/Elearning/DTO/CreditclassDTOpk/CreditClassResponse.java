package com.ptit.Elearning.DTO.CreditclassDTOpk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreditClassResponse {
    private Long creditClassId;

    private String startTime;

    private String endTime;

    private String schoolYear;

    private int status;

    private String joinedPassword;

    private int departmentId;

    private int subjectId;

    private List<String> teacherId;
}
