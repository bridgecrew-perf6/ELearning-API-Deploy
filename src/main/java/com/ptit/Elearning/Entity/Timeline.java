package com.ptit.Elearning.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "timeline")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Timeline {
    @EmbeddedId
    private TimelineId timelineId;

    @Column(name = "end_lesson")
    private int endLesson;

    @ManyToOne
    @JoinColumn(name = "credit_class_id",insertable = false,updatable = false)
    private CreditClass creditClass;

    @ManyToOne
    @JoinColumn(name = "room_id",insertable = false,updatable = false)
    private Room room;
}
