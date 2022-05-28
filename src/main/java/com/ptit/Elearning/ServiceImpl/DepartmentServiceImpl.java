package com.ptit.Elearning.ServiceImpl;

import com.ptit.Elearning.Entity.Department;
import com.ptit.Elearning.Repository.DepartmentRepository;
import com.ptit.Elearning.Exception.NotFoundException;
import com.ptit.Elearning.Service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    DepartmentRepository departmentRepository;
    @Override
    public Department getById(int departmentId) {
        return departmentRepository.findByDepartmentId(departmentId).orElseThrow(()-> new NotFoundException("Can't found the department with Id: "+departmentId));
    }

    @Override
    public List<Department> getAll() {
        return departmentRepository.findAll();
    }
}
