package com.ptit.Elearning.Service;

import com.ptit.Elearning.Entity.Notification;
import com.ptit.Elearning.Entity.UserInfo;
import org.springframework.data.domain.Page;

public interface NotificationService {
    public Notification createNewNotification(Notification notification);
    public Long getUnseenNotification(UserInfo userInfo);
    public boolean deleteByUserInfoAndNotId(UserInfo userInfo,Long notId);
    public Notification setSeenNotification(UserInfo userInfo,long notId);
    public Page<Notification> getAllNotification(UserInfo userInfo,int pageNo, int pageSize, String sortField, String sortDirection);
}
