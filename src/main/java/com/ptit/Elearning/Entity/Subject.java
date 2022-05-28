package com.ptit.Elearning.Entity;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "subject")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private int subjectId;

    @Column(name = "subject_name")
    private String subjectName;
}
