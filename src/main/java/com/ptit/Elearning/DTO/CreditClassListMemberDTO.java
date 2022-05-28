package com.ptit.Elearning.DTO;

import com.ptit.Elearning.DTO.StudentDTOpk.StudentDTO;
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
