package com.ptit.Elearning.DTO.SubmitDTOpk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SubmitRequest {
    private long excerciseId;
    private String submitContent;
    private MultipartFile file;
}
