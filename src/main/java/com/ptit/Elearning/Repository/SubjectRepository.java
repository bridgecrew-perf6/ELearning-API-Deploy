package com.ptit.Elearning.Repository;

import com.ptit.Elearning.Entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject,Integer> {
    public Optional<Subject> findById(int id);
}
