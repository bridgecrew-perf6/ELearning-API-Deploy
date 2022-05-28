package com.ptit.Elearning.ServiceImpl;

import com.ptit.Elearning.Entity.Submit;
import com.ptit.Elearning.Exception.NotFoundException;
import com.ptit.Elearning.Repository.SubmitRepository;
import com.ptit.Elearning.Service.SubmitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubmitServiceImpl implements SubmitService {
    @Autowired
    SubmitRepository submitRepository;

    @Override
    public Submit getSubmitWithExcerciseIdAndUserId(Long excerciseId, Long userId) {
        Submit submit = submitRepository.findBySubmitIdExcerciseIdAndSubmitIdUserId(excerciseId,userId).orElseThrow(()->new NotFoundException("Could not found the submit!"));
        return submit;
    }

    @Override
    public Submit markStudentPoint(Submit submit) {
        return submitRepository.save(submit);
    }

    @Override
    public List<Submit> getSubmitWithExcerciseId(Long excerciseId) {
        return submitRepository.findBySubmitIdExcerciseId(excerciseId);
    }

    @Override
    public Submit submit(Submit submit) {
        return submitRepository.save(submit);
    }

    @Override
    public boolean delete(Submit submit) {
        try{
            submitRepository.delete(submit);
            return true;
        }catch(Exception e){
            return true ;
        }
    }
}
