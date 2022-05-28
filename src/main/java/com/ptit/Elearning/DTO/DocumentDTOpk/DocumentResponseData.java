package com.ptit.Elearning.DTO.DocumentDTOpk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentResponseData {
    private String fileName;
    private String dowloadURL;
    private String fileType;
    private long fileSize;
}
