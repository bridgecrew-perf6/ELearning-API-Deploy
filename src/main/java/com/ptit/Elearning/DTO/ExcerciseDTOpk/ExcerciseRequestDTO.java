package com.ptit.Elearning.DTO.ExcerciseDTOpk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExcerciseRequestDTO {
    @Size(max = 200)
    private String title;
    @Size(max = 200)
    private String excerciseContent;

    private Timestamp startTime;
    private Timestamp endTime;
    private Long creditClassId;
    private MultipartFile files[];
}
