package com.ptit.Elearning.Controller.Admin;

import com.ptit.Elearning.Constant.ERole;
import com.ptit.Elearning.DTO.DocumentDTOpk.DocumentDTOForUpdate;
import com.ptit.Elearning.DTO.DocumentDTOpk.DocumentResponseData;
import com.ptit.Elearning.Entity.*;
import com.ptit.Elearning.Exception.NotFoundException;
import com.ptit.Elearning.Payloads.Request.Security.Jwt.JwtUtils;
import com.ptit.Elearning.Repository.RoleRepository;
import com.ptit.Elearning.Service.*;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Set;

@CrossOrigin(value = "*",maxAge = 3600)
@RestController
@RequestMapping("/api/admin/document")
public class ManageDocument {

    @Autowired
    DocumentService documentService;

    @Autowired
    FolderService folderService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AccountService accountService;

    @Autowired
    TeacherService teacherService;

    @ApiOperation(value = "Thêm tài liệu cho folder #49")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('TEACHER')")
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file")MultipartFile file, @RequestParam("folder-id") Long folderId, HttpServletRequest request){
        Document document=null;
        String dowloadURL="";
        try{
            //check folder exist
            Folder folder = folderService.getFolderById(folderId);

            java.lang.String username = jwtUtils.getUserNameFromJwtToken(request.getHeader("Authorization").substring(7));
            Account account = accountService.getAccountByUsername(username);
            Teacher teacher = null;
            CreditClass creditClass = folder.getCreditClass();

            //nếu là giảng viên(không phải là moderator ) thì phải dạy lớp đó mới được quyền upload file
            if(isTeacherAndNotModerator(account)){
                teacher = teacherService.getByUserInfo(account.getUserInfo());
                if(!teacher.getCreditClasses().contains(creditClass) ) return ResponseEntity.badRequest().body("You do not have permission!");
            }
            System.out.println(isSupportedContentType(StringUtils.cleanPath(file.getOriginalFilename())));
            document = documentService.saveDocument(file,folder);
            dowloadURL = ServletUriComponentsBuilder.fromCurrentContextPath().path("api/admin/document/dowload/")
                    .path(document.getDocumentId().toString())
                    .toUriString();

            DocumentResponseData documentResponseData = new DocumentResponseData(document.getDocumentName(),dowloadURL,file.getContentType(),file.getSize());

            return new ResponseEntity<>(documentResponseData,HttpStatus.CREATED);
        }catch (NotFoundException e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        }catch (SizeLimitExceededException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("File name is not valid");
        }


    }
    @ApiOperation(value = "Sửa tên tài liệu #50")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('TEACHER')")
    @PutMapping("/update-file-name")
    public ResponseEntity<?> updateFileName(@Valid @RequestBody DocumentDTOForUpdate documentDTO, HttpServletRequest request){
        Document document=null;
        String dowloadURL="";
        try{
            //check folder exist
            document = documentService.getDocument(documentDTO.getDocumentId());
            Folder folder = document.getFolder();

            java.lang.String username = jwtUtils.getUserNameFromJwtToken(request.getHeader("Authorization").substring(7));
            Account account = accountService.getAccountByUsername(username);
            Teacher teacher = null;
            CreditClass creditClass = folder.getCreditClass();

            //nếu là giảng viên(không phải là moderator ) thì phải dạy lớp đó mới được quyền cập nhật
            if(isTeacherAndNotModerator(account)){
                teacher = teacherService.getByUserInfo(account.getUserInfo());
                if(!teacher.getCreditClasses().contains(creditClass) ) return ResponseEntity.badRequest().body("You do not have permission!");
            }
            document.setDocumentName(documentDTO.getDocumentName());
            documentService.updateDocument(document);



            return new ResponseEntity<>("Update successfully!",HttpStatus.OK);
        }catch (NotFoundException e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("File name is not valid");
        }


    }
    @ApiOperation(value = "Xóa tài liệu #51")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('TEACHER')")
    @DeleteMapping("/delete-document")
    public ResponseEntity<?> deleteFile(@RequestParam("document-id") Long documentId, HttpServletRequest request){
        Document document=null;
        String dowloadURL="";
        try{
            //check folder exist
            document = documentService.getDocument(documentId);

            Folder folder = document.getFolder();

            java.lang.String username = jwtUtils.getUserNameFromJwtToken(request.getHeader("Authorization").substring(7));
            Account account = accountService.getAccountByUsername(username);
            Teacher teacher = null;
            CreditClass creditClass = folder.getCreditClass();

            //nếu là giảng viên(không phải là moderator ) thì phải dạy lớp đó mới được quyền cập nhật
            if(isTeacherAndNotModerator(account)){
                teacher = teacherService.getByUserInfo(account.getUserInfo());
                if(!teacher.getCreditClasses().contains(creditClass) ) return ResponseEntity.badRequest().body("You do not have permission!");
            }
            boolean result = documentService.deleteDocument(document);
            if(result){
                return new ResponseEntity<>("Deleted successfully!",HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("Somethings go wrong!",HttpStatus.UNPROCESSABLE_ENTITY);
            }


        }catch (NotFoundException e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Somethings go wrong!");
        }


    }

    @ApiOperation(value = "Lấy tài liệu dựa theo mã tài liệu #68")
    @GetMapping("/dowload/{document-id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('TEACHER') or hasRole('USER')")
    public ResponseEntity<?> dowloadFile(@PathVariable("document-id") Long documentId){
        Document document=null;

        try{
            document = documentService.getDocument(documentId);
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(document.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,"atachment;filename=\""+document.getDocumentName()+"\"")
                    .body(new ByteArrayResource(document.getData()));

        }catch (NotFoundException e){
            return ResponseEntity.notFound().build();
        }


    }
    private boolean isTeacherAndNotModerator(Account account) {

        Set<Role> roles = account.getRoles();
        Role roleTeacher = roleRepository.findByRoleName(ERole.ROLE_TEACHER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        Role roleMod = roleRepository.findByRoleName(ERole.ROLE_MODERATOR).orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        if (roles.contains(roleTeacher) && !roles.contains(roleMod) ){
            return true;
        }


        return false;
    }
    private boolean isSupportedContentType(String contentType) {
        return contentType.equals("png")
                || contentType.equals("jpg")
                || contentType.equals("jpeg")|| contentType.equals("doc")
                || contentType.equals("docx")
                || contentType.equals("xls") || contentType.equals("xlsx")
                || contentType.equals("ppt")|| contentType.equals("pptx");
    }

}
