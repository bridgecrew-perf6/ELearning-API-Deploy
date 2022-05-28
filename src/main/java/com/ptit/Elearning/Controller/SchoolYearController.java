package com.ptit.Elearning.Controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(value = "*",maxAge = 3600)
@RestController
@RequestMapping("/api/school-year")
public class SchoolYearController {

    @Autowired
    @Qualifier("schoolYear")
    private List<String> schoolYears;

    @ApiOperation("Lấy danh sách các năm học #52")
    @GetMapping("/get-all")
    public ResponseEntity<List<String>> getAllSchoolYear(){
        return ResponseEntity.ok(schoolYears);
    }
}
