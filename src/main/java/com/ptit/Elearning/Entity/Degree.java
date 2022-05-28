package com.ptit.Elearning.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "degree")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Degree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "degree_id")
    private int degreeId;
    @Column(name = "degree_name",length = 30)
    private String degreeName;
}
