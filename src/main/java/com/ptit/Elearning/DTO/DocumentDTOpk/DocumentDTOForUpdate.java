package com.ptit.Elearning.DTO.DocumentDTOpk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DocumentDTOForUpdate {
    private Long documentId;

    @NotBlank
    private String documentName;
}
