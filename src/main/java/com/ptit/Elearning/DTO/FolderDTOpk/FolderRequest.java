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
public class FolderRequest {

    private Long creditClassId;
    @NotBlank
    private String folderName;
    private Long parentId;

}
