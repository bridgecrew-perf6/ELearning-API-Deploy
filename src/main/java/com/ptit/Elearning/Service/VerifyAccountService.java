package com.ptit.Elearning.Service;

import com.ptit.Elearning.Entity.UserInfo;
import com.ptit.Elearning.Entity.VerifyAccount;

import java.util.UUID;

public interface VerifyAccountService {
    public VerifyAccount getById(UUID id);
    public VerifyAccount getByUserInfo(UserInfo userInfo);
    public VerifyAccount saveVerifyAccount(VerifyAccount verifyAccount);
}
