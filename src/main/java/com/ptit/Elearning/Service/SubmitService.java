package com.ptit.Elearning.Service;

import com.ptit.Elearning.Entity.Submit;

import java.util.List;

public interface SubmitService {
    public Submit getSubmitWithExcerciseIdAndUserId(Long excerciseId,Long userId);
    public Submit markStudentPoint(Submit submit);
    public List<Submit> getSubmitWithExcerciseId(Long excerciseId);
    public Submit submit(Submit submit);
    public boolean delete(Submit submit);
}
