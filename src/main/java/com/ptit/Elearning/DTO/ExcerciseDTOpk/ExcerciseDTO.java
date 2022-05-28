package com.ptit.Elearning.DTO.ExcerciseDTOpk;

import com.ptit.Elearning.DTO.DocumentDTOpk.DocumentDTO;
import com.ptit.Elearning.DTO.DocumentDTOpk.DocumentResponseDTODetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExcerciseDTO {
    private Long excerciseId;
    private String title;
    private String excerciseContent;
    private Timestamp startTime;
    private Timestamp endTime;
    private Set<DocumentResponseDTODetail> documents = new HashSet<>();
}
