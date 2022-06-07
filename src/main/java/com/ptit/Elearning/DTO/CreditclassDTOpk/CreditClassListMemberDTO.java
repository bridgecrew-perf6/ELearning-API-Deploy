package com.ptit.Elearning.DTO.CreditclassDTOpk;

import com.ptit.Elearning.DTO.StudentDTOpk.StudentDTO;
import com.ptit.Elearning.DTO.TeacherDTOpk.TeacherInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreditClassListMemberDTO {
    private List<TeacherInfoDTO> teacherInfos;
    private List<StudentDTO> students;

}
