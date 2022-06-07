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
public class CreditClassDTO {
    private Long creditClassId;
    private String subjectName;
    private List<String> teachers;
    private String departmentName;
    private String schoolYear;
    private int semester;
}
