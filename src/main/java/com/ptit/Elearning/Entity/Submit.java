package com.ptit.Elearning.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.sql.Timestamp;

@Entity
@Table(name = "submit")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Submit {
    @EmbeddedId
    private SubmitId submitId;

    @Column(name = "submit_content",length = 500)
    private String submitContent;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "document_id")
    private Document document;

    @Column(name = "submit_time")
    private Timestamp submitTime;

    @Min(0)
    @Max(10)
    private float mark;

    @ManyToOne
    @JoinColumn(name = "excercise_id",insertable = false,updatable = false)
    private Excercise excercise;

    @ManyToOne
    @JoinColumn(name = "user_id",insertable = false,updatable = false)
    private UserInfo userInfo;
}
