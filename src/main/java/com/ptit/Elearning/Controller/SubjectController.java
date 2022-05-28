package com.ptit.Elearning.Controller;

import com.ptit.Elearning.Entity.Subject;
import com.ptit.Elearning.Service.SubjectService;
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
@RequestMapping("/api/subject")
public class SubjectController {

    @Autowired
    SubjectService subjectService;

    @ApiOperation(value="Lấy danh sách của môn học #21")
    @GetMapping("/all")
    public ResponseEntity<List<Subject>> getAllSubject(){
        return ResponseEntity.ok(subjectService.getAll());
    }
}
