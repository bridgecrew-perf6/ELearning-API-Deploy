package com.ptit.Elearning.ServiceImpl;

import com.ptit.Elearning.Entity.Subject;
import com.ptit.Elearning.Exception.NotFoundException;
import com.ptit.Elearning.Repository.SubjectRepository;
import com.ptit.Elearning.Service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    SubjectRepository subjectRepository;

    @Override
    public List<Subject> getAll() {
        return subjectRepository.findAll();
    }

    @Override
    public Subject getById(int id) {
        return subjectRepository.findById(id).orElseThrow(()->new NotFoundException("Could not found subject"));
    }
}
