package com.ptit.Elearning.Repository;

import com.ptit.Elearning.Entity.Notification;
import com.ptit.Elearning.Entity.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    public long countByOwnerAndStatus(UserInfo userInfo,boolean status);
    public Optional<Notification> findByOwnerAndNotificationId(UserInfo userInfo, Long postId);
    public Page<Notification> findByOwnerOrderByNotificationIdDesc(UserInfo userInfo, Pageable pageable);
}
