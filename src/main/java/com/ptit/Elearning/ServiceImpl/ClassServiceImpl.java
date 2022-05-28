package com.ptit.Elearning.ServiceImpl;

import com.ptit.Elearning.Exception.NotFoundException;
import com.ptit.Elearning.Repository.ClassRepository;
import com.ptit.Elearning.Entity.Class;
import com.ptit.Elearning.Service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    ClassRepository classRepository;

    @Override
    public List<Class> getAllClass() {
        return classRepository.findAll();
    }

    @Override
    public Class getByClassId(String classId) {
        return classRepository.findByClassId(classId).orElseThrow(()->new NotFoundException("Could not find class with id: "+classId));
    }

}
