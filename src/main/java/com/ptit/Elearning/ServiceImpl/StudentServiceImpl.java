package com.ptit.Elearning.ServiceImpl;

import com.ptit.Elearning.Entity.Class;
import com.ptit.Elearning.Entity.Student;
import com.ptit.Elearning.Entity.UserInfo;
import com.ptit.Elearning.Exception.NotFoundException;
import com.ptit.Elearning.Repository.StudentRepository;
import com.ptit.Elearning.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    StudentRepository studentRepository;
    @Override
    public Student getByUserInfo(UserInfo userInfo) {
        return studentRepository.findByUserInfo(userInfo);
    }

    @Override
    public Student getByStudentCode(String studentCode) {
        return studentRepository.findByStudentCode(studentCode).orElseThrow(()-> new NotFoundException("Could not find student with code: "+studentCode));
    }

    @Override
    public List<Student> getByClass(Class classOf) {
        return studentRepository.findByClassOf(classOf);
    }

    @Override
    public List<Student> searchByStudentCode(String studentCode) {
        return studentRepository.findByStudentCodeContainsAllIgnoreCaseOrderByStudentCode(studentCode);
    }


}
