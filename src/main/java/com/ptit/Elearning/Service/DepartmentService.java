package com.ptit.Elearning.Service;

import com.ptit.Elearning.Entity.Department;

import java.util.List;

public interface DepartmentService {
    public Department getById(int departmentId);
    public List<Department> getAll();
}
