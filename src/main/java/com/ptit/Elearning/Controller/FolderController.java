package com.ptit.Elearning.Controller;

import com.ptit.Elearning.DTO.DocumentDTOpk.DocumentDTO;
import com.ptit.Elearning.DTO.FolderDTOpk.FolderDTOResponse;
import com.ptit.Elearning.DTO.FolderDTOpk.FolderRequest;
import com.ptit.Elearning.DTO.FolderDTOpk.FolderRequestForUpdate;
import com.ptit.Elearning.Entity.CreditClass;
import com.ptit.Elearning.Entity.Folder;
import com.ptit.Elearning.Exception.NotFoundException;
import com.ptit.Elearning.Service.CreditClassService;
import com.ptit.Elearning.Service.FolderService;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@CrossOrigin(value = "*",maxAge = 3600)
@RequestMapping("/api/folder")
public class FolderController {
    @Autowired
    FolderService folderService;

    @Autowired
    CreditClassService creditClassService;

    @Autowired
    ModelMapper modelMapper;

    @ApiOperation(value = "Tạo folder mới trong lớp tín chỉ\n Lưu ý: khi cho parentId= 0 thì tức là folder đang tạo không có folder cha! #46")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('TEACHER')")
    @PostMapping("/create-new-folder")
    public ResponseEntity<?> createNewFolder(@Valid @RequestBody FolderRequest folderRequest){
        CreditClass creditClass = null;
        Folder folder = null;
        try{
            creditClass = creditClassService.getCreditClassById(folderRequest.getCreditClassId());
        }catch (NotFoundException e){
            return ResponseEntity.notFound().build();
        }

        //folder mới không có folder cha: cho parent_id = 0
        if (folderRequest.getParentId()==0){
             folder = folderService.createNewFolderWithoutParents(creditClass,folderRequest.getFolderName());
        }
        //có folder cha:
        else{
            Folder parents;
            try{
              parents   = folderService.getFolderById(folderRequest.getParentId());
            }catch (NotFoundException e){
                return ResponseEntity.notFound().build();
            }
             folder = null;
            try{
              folder   = folderService.createNewFolder(creditClass,folderRequest.getFolderName(),parents);
            }catch (IllegalAccessException e){
                return ResponseEntity.unprocessableEntity().body("Could not create this folder because parent folder is not within creditclass");
            }
        }

        return new ResponseEntity<FolderDTOResponse>(convertToFolderDTO(folder), HttpStatus.CREATED);

    }

    @ApiOperation(value = "Cập nhật tên folder #47")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('TEACHER')")
    @PutMapping("/update-folder-name")
    public ResponseEntity<?> updateFolderName(@Valid @RequestBody FolderRequestForUpdate folderRequest){
        Folder folder = null;
        try{
            folder = folderService.getFolderById(folderRequest.getFolderId());
        }catch (NotFoundException e){
            return ResponseEntity.notFound().build();
        }
        folder.setFolderName(folderRequest.getFolderName());
        FolderDTOResponse response = convertToFolderDTO(folderService.updateFolderName(folder));
        return new ResponseEntity<FolderDTOResponse>(response, HttpStatus.OK);

    }
    @ApiOperation(value = "Xóa folder #48")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('TEACHER')")
    @DeleteMapping("/delete-folder")
    public ResponseEntity<?> deleteFolder(@RequestParam("folder-id") Long folderId){
        Folder folder = null;
        try{
            folder = folderService.getFolderById(folderId);
        }catch (NotFoundException e){
            return ResponseEntity.notFound().build();
        }

        boolean resultDelete = folderService.deleteFolder(folder);
        if(resultDelete)
            return new ResponseEntity<FolderDTOResponse>(new FolderDTOResponse(),HttpStatus.OK);
        else
            return new ResponseEntity<FolderDTOResponse>(new FolderDTOResponse(),HttpStatus.CONFLICT);

    }
    public FolderDTOResponse convertToFolderDTO(Folder folder){

            FolderDTOResponse response = new FolderDTOResponse();
            response.setFolderId(folder.getFolderId());
            response.setFolderName(folder.getFolderName());
            response.setDocuments(modelMapper.map(folder.getDocuments(),new TypeToken<Set<DocumentDTO>>(){}.getType()));
            if(folder.getParentsFolder()!=null){
                response.setParentsFolder(folder.getParentsFolder().getFolderId());
            }
            response.setUpTime(folder.getUpTime());

        return response;
    }
}
