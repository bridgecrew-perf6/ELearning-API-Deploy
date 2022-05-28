package com.ptit.Elearning.Repository;

import com.ptit.Elearning.Entity.Teacher;
import com.ptit.Elearning.Entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher,String> {
    public Optional<Teacher> findByUserInfo(UserInfo userInfo);
    public Optional<Teacher> findById(String teacherId);
}
