package com.ptit.Elearning.Repository;

import com.ptit.Elearning.Entity.CreditClass;
import com.ptit.Elearning.Entity.Excercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExcerciseRepository extends JpaRepository<Excercise,Long> {
    public List<Excercise> findByCreditClassOrderByExcerciseIdDesc(CreditClass creditClass);
    public Optional<Excercise> findByExcerciseId(long excerciseId);

    @Query(value = "Select * from excercise where excercise_id = (SELECT MAX(excercise_id) from excercise)",nativeQuery = true)
    public Excercise getExcerciseWithIdMax();
}
