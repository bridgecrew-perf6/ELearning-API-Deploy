package com.ptit.Elearning.Repository;

import com.ptit.Elearning.Entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface DepartmentRepository extends JpaRepository<Department,Integer> {
    public Optional<Department> findByDepartmentId(int departmentId);
}
