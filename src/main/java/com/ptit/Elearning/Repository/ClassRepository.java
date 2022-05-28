package com.ptit.Elearning.Repository;

import com.ptit.Elearning.Entity.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<Class,String> {
    public Optional<Class> findByClassId(String classId);
}
