package com.ptit.Elearning.Controller;

import com.ptit.Elearning.Entity.Class;
import com.ptit.Elearning.Service.ClassService;
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
@RequestMapping("/api/class")
public class ClassController {
    @Autowired
    ClassService classService;

    @ApiOperation("Lấy danh sách các lớp #53")
    @GetMapping("/all")
    public ResponseEntity<List<Class>> getAllClass(){
        return ResponseEntity.ok(classService.getAllClass());
    }
}
