package com.ptit.Elearning.Service;

import com.ptit.Elearning.Entity.Teacher;
import com.ptit.Elearning.Entity.UserInfo;

public interface TeacherService {
    public Teacher getByUserInfo(UserInfo userInfo);
    public Teacher getTeacherById(String teacherId);
}
