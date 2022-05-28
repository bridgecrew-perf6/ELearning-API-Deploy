package com.ptit.Elearning.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class TimelineId implements Serializable {
    @Column(name = "credit_class_id")
    private Long creditClassId;

    @Column(name = "day_of_week")
    private int dayOfWeek;

    @Column(name = "start_lesson")
    private int startLesson;

    @Column(name = "room_id")
    private int roomId;

    public TimelineId(Long creditClassId, int dayOfWeek, int startLesson, int roomId) {
        this.creditClassId = creditClassId;
        this.dayOfWeek = dayOfWeek;
        this.startLesson = startLesson;
        this.roomId = roomId;
    }

    public TimelineId() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimelineId that = (TimelineId) o;
        return dayOfWeek == that.dayOfWeek && startLesson == that.startLesson && roomId == that.roomId && creditClassId.equals(that.creditClassId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creditClassId, dayOfWeek, startLesson, roomId);
    }
}
