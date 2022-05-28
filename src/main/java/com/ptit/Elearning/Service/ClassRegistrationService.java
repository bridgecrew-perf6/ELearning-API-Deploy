package com.ptit.Elearning.Service;

import com.ptit.Elearning.DTO.ListStudentIDDTO;
import com.ptit.Elearning.Entity.ClassRegistration;
import com.ptit.Elearning.Entity.CreditClass;
import com.ptit.Elearning.Entity.Teacher;
import com.ptit.Elearning.Entity.UserInfo;

import java.util.List;

public interface ClassRegistrationService {
    public List<ClassRegistration> getListClassByUserInfo(UserInfo userInfo);
    public List<UserInfo> getListUserInfosByCreditClass(CreditClass creditClass);
    public List<UserInfo> getListUserInfosByCreditClassActive(CreditClass creditClass);
    public boolean addStudentToCreditClass(CreditClass creditClass, ListStudentIDDTO list, Teacher teacher);
    public boolean removeStudentFromCreditClass(CreditClass creditClass, ListStudentIDDTO list,Teacher teacher);
    public boolean checkStudentJoinedClass(Long userId,Long creditClassId);
    public boolean joinToCreditClass(CreditClass creditClass,UserInfo userInfo, String joinCode);

}
