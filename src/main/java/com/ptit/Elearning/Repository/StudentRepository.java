package com.ptit.Elearning.Repository;

import com.ptit.Elearning.Entity.Class;
import com.ptit.Elearning.Entity.Student;
import com.ptit.Elearning.Entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository  extends JpaRepository<Student,String> {
    public Student findByUserInfo(UserInfo userInfo);
    public Optional<Student> findByStudentCode(String studentCode);
    public List<Student> findByClassOf(Class classOf);
    public List<Student> findByStudentCodeContainsAllIgnoreCaseOrderByStudentCode(String studentCode);
}
