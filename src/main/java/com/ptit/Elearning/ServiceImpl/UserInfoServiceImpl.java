package com.ptit.Elearning.ServiceImpl;

import com.ptit.Elearning.Entity.UserInfo;
import com.ptit.Elearning.Repository.UserInfoRespository;
import com.ptit.Elearning.Service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    UserInfoRespository userInfoRespository;

    @Override
    public UserInfo saveUserInfo(UserInfo userInfo) {
        return userInfoRespository.save(userInfo);
    }

    @Override
    public List<UserInfo> getAllUser(int pageNo, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortField).ascending() : Sort.by(sortField).descending() ;

        Pageable pageable = PageRequest.of(pageNo -1, pageSize,sort);
        return userInfoRespository.findAll(pageable).getContent();
    }

    @Override
    public List<UserInfo> searchUser(String value) {
        return userInfoRespository.findByEmailContainsOrFullnameContainsOrPhoneContainsAllIgnoreCase(value,value,value);
    }
}
