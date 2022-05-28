package com.ptit.Elearning.DTO.NotificationDTOPk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificationPageForUser {

    private int totalPage;
    List<NotificationDTO> notifications;
}
