package com.ptit.Elearning.ServiceImpl;

import com.ptit.Elearning.Entity.UserInfo;
import com.ptit.Elearning.Entity.VerifyAccount;
import com.ptit.Elearning.Exception.NotFoundException;
import com.ptit.Elearning.Repository.VerifyAccountRepository;
import com.ptit.Elearning.Service.VerifyAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VerifyAccountServiceImpl implements VerifyAccountService {
    @Autowired
    VerifyAccountRepository verifyAccountRepository;

    @Override
    public VerifyAccount getById(UUID id) {
        return verifyAccountRepository.findById(id).orElseThrow(()-> new NotFoundException("Could not find Verify Account"));
    }

    @Override
    public VerifyAccount getByUserInfo(UserInfo userInfo) {
        return verifyAccountRepository.findByUserInfo(userInfo).orElseThrow(()-> new NotFoundException("Could not find Verify Account"));

    }

    @Override
    public VerifyAccount saveVerifyAccount(VerifyAccount verifyAccount) {
        return verifyAccountRepository.save(verifyAccount);
    }
}
