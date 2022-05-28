package com.ptit.Elearning.DTO;

import com.ptit.Elearning.DTO.DocumentDTOpk.DocumentDTO;
import com.ptit.Elearning.DTO.DocumentDTOpk.DocumentResponseDTODetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExcerciseDTODetailForSubmit {
    private Long excerciseId;
    private String excerciseTitle;
    private String excerciseContent;
    private String startTime;
    private String endTime;
    private String submitTime;
    private String submitContent;
    private List<DocumentResponseDTODetail> documents;
    private DocumentResponseDTODetail submitFile;
}
