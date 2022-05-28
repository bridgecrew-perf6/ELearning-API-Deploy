package com.ptit.Elearning.Repository;

import com.ptit.Elearning.Entity.Submit;
import com.ptit.Elearning.Entity.SubmitId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubmitRepository extends JpaRepository<Submit, SubmitId> {
    public Optional<Submit> findBySubmitIdExcerciseIdAndSubmitIdUserId(Long excerciseId, Long userId);
    public List<Submit> findBySubmitIdExcerciseId(Long excerciseId);
}
