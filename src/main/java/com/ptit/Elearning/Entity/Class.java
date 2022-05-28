package com.ptit.Elearning.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "class")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Class {
    @Id
    @Column(name = "class_id")
    private String classId;

    @Column(name = "class_name")
    private String className;

    @Column(name = "school_year")
    private String schoolYear;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}
