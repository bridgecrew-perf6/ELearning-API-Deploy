package com.ptit.Elearning.DTO.FolderDTOpk;

import com.ptit.Elearning.DTO.DocumentDTOpk.DocumentDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FolderDTOResponse {
    private Long folderId;
    private String folderName;
    private Date upTime;
    private Long parentsFolder;
    private Set<DocumentDTO> documents = new HashSet<>();
}
