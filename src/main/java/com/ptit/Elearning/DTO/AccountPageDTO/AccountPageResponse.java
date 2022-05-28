package com.ptit.Elearning.DTO.AccountPageDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountPageResponse {
    private int totalPage;
    private List<AccountAndUserInfo> accountsInfo;
}
