package com.ptit.Elearning.Repository;

import com.ptit.Elearning.Entity.Account;
import com.ptit.Elearning.Entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    Optional<Account> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByUserInfo(UserInfo userInfo);
    Optional<Account> findByUserInfo(UserInfo userInfo);
}
