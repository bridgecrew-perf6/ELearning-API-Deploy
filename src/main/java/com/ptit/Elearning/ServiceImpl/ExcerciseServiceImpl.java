package com.ptit.Elearning.ServiceImpl;

import com.ptit.Elearning.Entity.CreditClass;
import com.ptit.Elearning.Entity.Excercise;
import com.ptit.Elearning.Exception.NotFoundException;
import com.ptit.Elearning.Repository.ExcerciseRepository;
import com.ptit.Elearning.Service.ExcerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExcerciseServiceImpl implements ExcerciseService {

    @Autowired
    ExcerciseRepository excerciseRepository;
    @Override
    public List<Excercise> getByCreditClass(CreditClass creditClass) {
        return excerciseRepository.findByCreditClassOrderByExcerciseIdDesc(creditClass);
    }

    @Override
    public Excercise getByExcerciseId(long excerciseId) {
        return excerciseRepository.findByExcerciseId(excerciseId).orElseThrow(()-> new NotFoundException("Could not find excercise with id: "+excerciseId));
    }

    @Override
    public Excercise getExcerciseWithIdMax() {
        return excerciseRepository.getExcerciseWithIdMax();
    }

    @Override
    public Excercise saveExcercise(Excercise e) {
        return excerciseRepository.save(e);
    }
}
