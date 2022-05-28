package com.ptit.Elearning.Service;

import com.ptit.Elearning.Entity.Teacher;
import com.ptit.Elearning.Entity.UserInfo;

import java.util.List;

public interface TeacherService {
    public Teacher getByUserInfo(UserInfo userInfo);
    public Teacher getTeacherById(String teacherId);

    public List<Teacher> getAllTeachers();
}
