package com.ptit.Elearning.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "excercise")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Excercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "excercise_id")
    private Long excerciseId;

    private String title;

    @Column(name = "excercise_content",length = 500)
    private String excerciseContent;

    @Column(name = "start_time")
    private Timestamp startTime;

    @Column(name = "end_time")
    private Timestamp endTime;

    @ManyToOne
    @JoinColumn(name = "credit_class_id")
    private CreditClass creditClass;

    @ManyToMany
    @JoinTable(name = "excercise_document", joinColumns =@JoinColumn(name = "excercise_id"),inverseJoinColumns = @JoinColumn(name = "document_id"))
    private Set<Document> documents = new HashSet<>();
}
