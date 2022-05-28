package com.ptit.Elearning.Repository;

import com.ptit.Elearning.Entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInfoRespository extends JpaRepository<UserInfo,Long> {
    public UserInfo findUserInfoByUserId(Long userId);
    List<UserInfo> findByEmailContainsOrFullnameContainsOrPhoneContainsAllIgnoreCase(String email,String fullname,String phone);
}
