package com.ptit.Elearning.DTO.NotificationDTOPk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificationDTO {
    private Long notificationId;
    private String notificationContent;
    private Timestamp time;
    private boolean status;
}
