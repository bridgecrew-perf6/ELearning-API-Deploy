package com.ptit.Elearning.Controller;

import com.ptit.Elearning.Entity.Department;
import com.ptit.Elearning.Service.DepartmentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(value = "*",maxAge = 3600)
@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;
    @ApiOperation(value="Lấy danh sách các khoa của trường #34")
    @GetMapping("/all")
    public ResponseEntity<List<Department>> getAllDepartment(){
        return ResponseEntity.ok(departmentService.getAll());
    }
}