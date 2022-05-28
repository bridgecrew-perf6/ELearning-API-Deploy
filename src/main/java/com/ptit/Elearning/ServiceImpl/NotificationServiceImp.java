package com.ptit.Elearning.ServiceImpl;

import com.ptit.Elearning.Entity.Notification;
import com.ptit.Elearning.Exception.NotFoundException;
import com.ptit.Elearning.Repository.NotificationRepository;
import com.ptit.Elearning.Entity.UserInfo;
import com.ptit.Elearning.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImp implements NotificationService {
    @Autowired
    NotificationRepository notificationRepository;

    @Override
    public Notification createNewNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public Long getUnseenNotification(UserInfo userInfo) {
        return notificationRepository.countByOwnerAndStatus(userInfo,false);
    }

    @Override
    public boolean deleteByUserInfoAndNotId(UserInfo userInfo, Long notId) {
        Notification no = notificationRepository.findByOwnerAndNotificationId(userInfo,notId).orElseThrow(()->new NotFoundException("Could not found the suitable notification!"));
        notificationRepository.delete(no);
        return true;
    }

    @Override
    public Notification setSeenNotification(UserInfo userInfo,long notId) {
        Notification no = notificationRepository.findByOwnerAndNotificationId(userInfo,notId).orElseThrow(()->new NotFoundException("Could not found the suitable notification!"));
        no.setStatus(true);
        return notificationRepository.save(no);
    }

    @Override
    public Page<Notification> getAllNotification(UserInfo userInfo,int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortField).ascending() : Sort.by(sortField).descending() ;

        Pageable pageable = PageRequest.of(pageNo -1, pageSize,sort);
        return notificationRepository.findByOwnerOrderByNotificationIdDesc(userInfo,pageable);
    }
}
