package com.ptit.Elearning.Service;

import com.ptit.Elearning.Entity.UserInfo;

import java.util.List;

public interface UserInfoService {
    public UserInfo saveUserInfo(UserInfo userInfo);
    public List<UserInfo> getAllUser(int pageNo, int pageSize, String sortField, String sortDir);
    List<UserInfo> searchUser(String value);//by email, fullname, phone
}
