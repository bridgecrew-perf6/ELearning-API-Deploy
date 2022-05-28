package com.ptit.Elearning.Service;

import com.ptit.Elearning.Entity.CreditClass;
import com.ptit.Elearning.Entity.Excercise;

import java.util.List;

public interface ExcerciseService {
    public List<Excercise> getByCreditClass(CreditClass creditClass);
    public Excercise getByExcerciseId(long excerciseId);
    public Excercise getExcerciseWithIdMax();
    public Excercise saveExcercise(Excercise e);
}
