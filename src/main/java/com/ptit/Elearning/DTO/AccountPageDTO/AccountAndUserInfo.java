package com.ptit.Elearning.DTO.AccountPageDTO;

import com.ptit.Elearning.DTO.UserInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountAndUserInfo {
     private Long accountId;
    private String username;
    private UserInfoDTO userInfoDTO;
}
