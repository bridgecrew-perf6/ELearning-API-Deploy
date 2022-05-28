package com.ptit.Elearning.Service;

import com.ptit.Elearning.Entity.Class;

import java.util.List;

public interface ClassService {
    public List<Class> getAllClass();
    public Class getByClassId(String classId);
}
