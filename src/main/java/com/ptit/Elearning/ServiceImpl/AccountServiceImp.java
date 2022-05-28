package com.ptit.Elearning.ServiceImpl;

import com.ptit.Elearning.Entity.Account;
import com.ptit.Elearning.Repository.AccountRepository;
import com.ptit.Elearning.Entity.UserInfo;
import com.ptit.Elearning.Exception.NotFoundException;
import com.ptit.Elearning.Payloads.Request.Security.Jwt.JwtUtils;
import com.ptit.Elearning.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImp implements AccountService {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AccountRepository accountRepository;
    @Override
    public UserInfo getUserInfoByUsername(String username) {
        Account account = accountRepository.findByUsername(username).orElseThrow(()-> new NotFoundException("User not found with username: "+username));
        UserInfo userInfo = account.getUserInfo();
        return userInfo;
    }

    @Override
    public Account getAccountByUsername(String username) {
        return accountRepository.findByUsername(username).orElseThrow(()-> new NotFoundException("User not found with username: "+username));
    }

    @Override
    public Account saveAnExistingAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Page<Account> getAllAccount(int pageNo, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortField).ascending() : Sort.by(sortField).descending() ;

        Pageable pageable = PageRequest.of(pageNo -1, pageSize,sort);

        Page<Account> accountPage = null;
        try{
             accountPage = accountRepository.findAll(pageable);
        }catch (Exception e){
            System.out.println("error: "+e.getMessage());
        }

        return accountPage;
    }

    @Override
    public Account getByUserInfo(UserInfo userInfo) {
        return accountRepository.findByUserInfo(userInfo).orElseThrow(()->new NotFoundException("Account does't not exist !"));
    }


}
