package com.ptit.Elearning.ServiceImpl;

import com.ptit.Elearning.Exception.NotFoundException;
import com.ptit.Elearning.Repository.TeacherRepository;
import com.ptit.Elearning.Entity.Teacher;
import com.ptit.Elearning.Entity.UserInfo;
import com.ptit.Elearning.Service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    TeacherRepository teacherRepository;

    @Override
    public Teacher getByUserInfo(UserInfo userInfo) {
        return teacherRepository.findByUserInfo(userInfo).orElseThrow(()->new NotFoundException("Could not find teacher!"));
    }

    @Override
    public Teacher getTeacherById(String teacherId) {
        return teacherRepository.findById(teacherId).orElseThrow(()->new NotFoundException("Could not found the teacher with id: "+teacherId));
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }
}
