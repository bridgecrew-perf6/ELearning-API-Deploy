package com.ptit.Elearning.Service;

import com.ptit.Elearning.Entity.Subject;

import java.util.List;

public interface SubjectService {
    public List<Subject> getAll();
    public Subject getById(int id);
}
