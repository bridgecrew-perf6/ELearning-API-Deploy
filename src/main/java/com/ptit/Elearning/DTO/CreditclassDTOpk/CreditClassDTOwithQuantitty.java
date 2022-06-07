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
public class CreditClassDTOwithQuantitty {
    private Long creditClassId;
    private String subjectName;
    private String departmentName;
    private String schoolYear;
    private int semester;
    private int totalTeacher;
    private int totalStudent;
}
