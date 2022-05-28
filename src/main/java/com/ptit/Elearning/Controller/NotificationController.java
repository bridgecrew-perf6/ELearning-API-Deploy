package com.ptit.Elearning.Controller;

import com.ptit.Elearning.DTO.NotificationDTOPk.NotificationDTO;
import com.ptit.Elearning.DTO.NotificationDTOPk.NotificationPageForUser;
import com.ptit.Elearning.Entity.Account;
import com.ptit.Elearning.Entity.Notification;
import com.ptit.Elearning.Exception.NotFoundException;
import com.ptit.Elearning.Payloads.Request.Security.Jwt.JwtUtils;
import com.ptit.Elearning.Service.AccountService;
import com.ptit.Elearning.Service.NotificationService;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(value = "*",maxAge = 3600)
@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @Autowired
    AccountService accountService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    ModelMapper modelMapper;
    @ApiOperation(value = "Xóa một thông báo trong danh sách thông báo theo mã thông báo #28")
    @DeleteMapping
    public ResponseEntity<?> notification(HttpServletRequest request,@RequestParam(name = "notification-id") Long id){
        Account account = accountService.getAccountByUsername(jwtUtils.getUserNameFromJwtToken(request.getHeader("Authorization").substring(7)));
        try{
            notificationService.deleteByUserInfoAndNotId(account.getUserInfo(),id);
        }catch (NotFoundException e){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body("Delete notification successfully");
    }
    @ApiOperation(value="Cập nhật trạng thái 'đã xem' cho thông báo #27")
    @PutMapping("/seen")
    public ResponseEntity<?> setSeenForNotification(HttpServletRequest request,@RequestParam(name = "notification-id") Long id){
        Account account = accountService.getAccountByUsername(jwtUtils.getUserNameFromJwtToken(request.getHeader("Authorization").substring(7)));
        try{
           Notification notification = notificationService.setSeenNotification(account.getUserInfo(),id);
            return ResponseEntity.ok().body(notification.getNotificationContent());
        }catch (NotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
    @ApiOperation(value="Lấy số thông báo chưa xem của sinh viên #9")
    @GetMapping("/unseen-notification")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('TEACHER')")
    public ResponseEntity<Long> getUnSeenNotification(HttpServletRequest request){
        String username = jwtUtils.getUserNameFromJwtToken(request.getHeader("Authorization").substring(7));
        Account account = accountService.getAccountByUsername(username);
        Long quantityUnseenNotification = notificationService.getUnseenNotification(account.getUserInfo());
        return ResponseEntity.ok(quantityUnseenNotification);
    }
    @ApiOperation(value="Lấy thông báo của người dùng #26")
    @GetMapping(value = {"/all-notification","/all-notification/{pageNo}"})
    public ResponseEntity<NotificationPageForUser> getAllNotification(HttpServletRequest request, @PathVariable(required = false) Optional<Integer> pageNo){
        int pageNoOffical = 1;
        if(pageNo.isPresent()) pageNoOffical = pageNo.get();
        int pageSize = 6;
        String sortDir = "desc";
        String sortField = "time";
        String username = jwtUtils.getUserNameFromJwtToken(request.getHeader("Authorization").substring(7));
        Account account = accountService.getAccountByUsername(username);

        NotificationPageForUser page = new NotificationPageForUser();

        Page<Notification> listNotification = notificationService.getAllNotification(account.getUserInfo(),pageNoOffical,pageSize,sortField,sortDir);
        List<NotificationDTO> notifications = listNotification.getContent()
                .stream().map(n->modelMapper.map(n,NotificationDTO.class)).collect(Collectors.toList());

        page.setNotifications(notifications);
        page.setTotalPage(listNotification.getTotalPages());

        return ResponseEntity.ok(page);
    }
}
