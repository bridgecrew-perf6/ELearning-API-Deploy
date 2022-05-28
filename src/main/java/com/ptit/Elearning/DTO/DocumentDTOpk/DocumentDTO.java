package com.ptit.Elearning.DTO.DocumentDTOpk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.sql.Timestamp;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DocumentDTO {
    private Long documentId;
    private String documentName;
    private Timestamp createAt;
}
