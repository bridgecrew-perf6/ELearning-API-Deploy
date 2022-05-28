package com.ptit.Elearning.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "notification")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    @Column(name = "content")
    private String notificationContent;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserInfo owner;

    private Timestamp time;
    private boolean status;//true: seen; false: unseen
}
