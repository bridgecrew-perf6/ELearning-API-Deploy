package com.ptit.Elearning.DTO.FolderDTOpk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FolderRequestForUpdate {
    private Long folderId;

    @NotBlank
    private String folderName;
}
