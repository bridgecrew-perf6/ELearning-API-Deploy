package com.ptit.Elearning.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountDTO {
    private  Long accountId;
    private List<String> roles;
    private String userName;
}
