package com.ptit.Elearning.Entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "credit_class")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreditClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "credit_class_id")
    private Long creditClassId;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "school_year")
    private String schoolYear;

    @Column(name = "joined_password")
    private String joinedPassword;

    private int status;//0: cancel, 1: finish or onboarding

    @OneToMany(mappedBy = "creditClass")
    private Set<Folder> folders;

    @ManyToMany
    @JoinTable(name = "teach", joinColumns = @JoinColumn(name = "credit_class_id"), inverseJoinColumns = @JoinColumn(name = "teacher_id"))
    private Set<Teacher> teachers = new HashSet<>();

    @OneToMany(mappedBy = "creditClass",fetch = FetchType.EAGER)
    private Set<Post> posts = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

}
