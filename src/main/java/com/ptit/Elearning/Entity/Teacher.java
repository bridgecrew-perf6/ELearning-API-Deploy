package com.ptit.Elearning.Entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "teacher")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Teacher {
    @Id
    @Column(name = "teacher_id")
    private String teacherId;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "profession_id")
    private Profession profession;

    @ManyToOne
    @JoinColumn(name = "degree_id")
    private Degree degree;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserInfo userInfo;

    @ManyToMany
    @JoinTable(name = "teach", joinColumns = @JoinColumn(name = "teacher_id"), inverseJoinColumns = @JoinColumn(name = "credit_class_id"))
    private Set<CreditClass> creditClasses = new HashSet<>();
}
