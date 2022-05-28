package com.ptit.Elearning.Entity;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "student")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Student",description = "Đối tượng sinh viên")
public class Student {
    @Id
    @Column(name = "student_code")
    private String studentCode;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class classOf;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserInfo userInfo;
}
