package com.ptit.Elearning.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "profession")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Profession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="profession_id")
    private int professionId;

    @Column(name="profession_name",length = 30)
    private String professionName;
}
