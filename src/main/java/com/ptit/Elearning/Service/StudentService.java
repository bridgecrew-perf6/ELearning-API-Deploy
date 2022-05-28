package com.ptit.Elearning.Service;

import com.ptit.Elearning.Entity.Class;
import com.ptit.Elearning.Entity.Student;
import com.ptit.Elearning.Entity.UserInfo;

import java.util.List;

public interface StudentService {
    public Student getByUserInfo(UserInfo userInfo);
    public Student getByStudentCode(String studentCode);
    public List<Student> getByClass(Class classOf);
    public List<Student> searchByStudentCode(String studentCode);

}
