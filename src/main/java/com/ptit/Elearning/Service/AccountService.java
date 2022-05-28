package com.ptit.Elearning.Service;

import com.ptit.Elearning.Entity.Account;
import com.ptit.Elearning.Entity.UserInfo;
import org.springframework.data.domain.Page;

public interface AccountService {
    public UserInfo getUserInfoByUsername(String username);
    public Account getAccountByUsername(String username);
    public Account saveAnExistingAccount(Account account);
    public Page<Account> getAllAccount(int pageNo, int pageSize, String sortField, String sortDir);
    public Account getByUserInfo(UserInfo userInfo);
}
