package com.ptit.Elearning.DTO.CreditclassDTOpk;

import com.ptit.Elearning.DTO.ExcerciseDTOpk.ExcerciseDTO;
import com.ptit.Elearning.DTO.FolderDTOpk.FolderDTOResponse;
import com.ptit.Elearning.DTO.PostDTO.PostDTO;
import com.ptit.Elearning.DTO.TeacherDTOpk.TeacherInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreditClassDetailDTO {
    private String creditClassName;
    private List<TeacherInfoDTO> teacherInfos;
    private String departmentName;
    private String startTime;
    private String endTime;
    private List<ExcerciseDTO> excercises;
    private List<FolderDTOResponse> folders;
    private List<PostDTO> listPost;

}
