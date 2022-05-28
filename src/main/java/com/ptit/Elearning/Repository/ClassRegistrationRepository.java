package com.ptit.Elearning.Repository;

import com.ptit.Elearning.Entity.ClassRegistration;
import com.ptit.Elearning.Entity.ClassRegistrationId;
import com.ptit.Elearning.Entity.CreditClass;
import com.ptit.Elearning.Entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRegistrationRepository extends JpaRepository<ClassRegistration, ClassRegistrationId> {
    public List<ClassRegistration> findByUserInfo(UserInfo userInfo);
    public List<ClassRegistration> findByCreditClass(CreditClass creditClass);
    public List<ClassRegistration> findByCreditClassAndStatus(CreditClass creditClass,int status);
    public Optional<ClassRegistration> findByClassRegistrationIdUserIdAndClassRegistrationIdCreditClassId(Long userId, Long creditClassId);
}
