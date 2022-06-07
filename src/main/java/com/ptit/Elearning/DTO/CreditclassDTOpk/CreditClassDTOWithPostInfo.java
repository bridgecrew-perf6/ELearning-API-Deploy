package com.ptit.Elearning.DTO.CreditclassDTOpk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreditClassDTOWithPostInfo {
    private Long creditClassId;
    private String subjectName;
    private String departmentName;
    private String schoolYear;
    private int semester;
    private int totalPost;
    private int totalComment;
}
