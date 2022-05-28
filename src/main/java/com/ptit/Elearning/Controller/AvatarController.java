package com.ptit.Elearning.Controller;

import com.ptit.Elearning.DTO.DocumentDTOpk.DocumentResponseData;
import com.ptit.Elearning.Entity.Account;
import com.ptit.Elearning.Entity.UserInfo;
import com.ptit.Elearning.Exception.NotFoundException;
import com.ptit.Elearning.Payloads.Request.Security.Jwt.JwtUtils;
import com.ptit.Elearning.Service.AccountService;
import com.ptit.Elearning.Service.AvatarService;
import com.ptit.Elearning.Service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(value = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/avatar")

public class AvatarController {

    @Autowired
    AvatarService avatarService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AccountService accountService;

    @Autowired
    UserInfoService userInfoService;

    @PreAuthorize("hasRole('MODERATOR') or hasRole('TEACHER') or hasRole('USER')")
    @PostMapping(value="/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        com.ptit.Elearning.Entity.Avatar avatar = null;
        String dowloadURL = "";
        String username = jwtUtils.getUserNameFromJwtToken(request.getHeader("Authorization").substring(7));
        Account account = accountService.getAccountByUsername(username);
        try {
            avatar = avatarService.saveAvatar(file);
            dowloadURL = ServletUriComponentsBuilder.fromCurrentContextPath().path("api/avatar/dowload/")
                    .path(avatar.getAvatarId().toString())
                    .toUriString();

            UserInfo user = account.getUserInfo();
            user.setAvatar(avatar);

            userInfoService.saveUserInfo(user);

            DocumentResponseData avartResponseData = new DocumentResponseData(avatar.getFileName(), dowloadURL, file.getContentType(), file.getSize());

            return new ResponseEntity<>(avartResponseData, HttpStatus.CREATED);
        } catch (IllegalAccessException e) {
            return ResponseEntity.badRequest().body(e.toString());
        } catch (Exception e) {
            System.out.println("Message"+e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }
    //newly for android:
//    @PostMapping(value="/upload/token", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<?> uploadFileWithTokenParam( UploadAvatarRequest uploadAvatarRequest) {
//        com.ptit.Elearning.Entity.Avatar avatar = null;
//        String dowloadURL = "";
//        String username = jwtUtils.getUserNameFromJwtToken(uploadAvatarRequest.getToken().trim());
//        Account account = accountService.getAccountByUsername(username);
//        try {
//            avatar = avatarService.saveAvatar(uploadAvatarRequest.getFile());
//            dowloadURL = ServletUriComponentsBuilder.fromCurrentContextPath().path("api/avatar/dowload/")
//                    .path(avatar.getAvatarId().toString())
//                    .toUriString();
//
//            UserInfo user = account.getUserInfo();
//            user.setAvatar(avatar);
//
//            userInfoService.saveUserInfo(user);
//
//            DocumentResponseData avartResponseData = new DocumentResponseData(avatar.getFileName(), dowloadURL, uploadAvatarRequest.getFile().getContentType(), uploadAvatarRequest.getFile().getSize());
//
//            return new ResponseEntity<>(avartResponseData, HttpStatus.CREATED);
//        } catch (IllegalAccessException e) {
//            return ResponseEntity.badRequest().body(e.toString());
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("File name is not valid");
//        }
//    }



    @PreAuthorize("hasRole('MODERATOR') or hasRole('TEACHER') or hasRole('USER')")
    @GetMapping("/dowload/{avatar-id}")
    public ResponseEntity<?> getAvatar(@PathVariable("avatar-id") Long avatarId) {
        com.ptit.Elearning.Entity.Avatar avatar = null;

        try {
            avatar = avatarService.getAvatar(avatarId);
            System.out.println("load sucucessfully");
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(avatar.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "atachment;filename=\"" + avatar.getFileName() + "\"")
                    .body(new ByteArrayResource(avatar.getData()));

        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
