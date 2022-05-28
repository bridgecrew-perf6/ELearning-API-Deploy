package com.ptit.Elearning.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserInfoDTO {
   private  Long userId;
   private String avatar;
    private String fullname;
    private List<String> roles;
    private String email;
    private String phone;
    private String address;
    private boolean gender;
    private String dateOfBirth;
    private String userCode;
    private String userClass; //cant be 'x': 'x' means empty
}
