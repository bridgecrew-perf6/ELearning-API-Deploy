package com.ptit.Elearning.Repository;

import com.ptit.Elearning.Entity.UserInfo;
import com.ptit.Elearning.Entity.VerifyAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface VerifyAccountRepository extends JpaRepository<VerifyAccount, UUID> {
    public Optional<VerifyAccount> findById(UUID id);
    public Optional<VerifyAccount> findByUserInfo(UserInfo userInfo);

}
