package com.ptit.Elearning.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubmitId implements Serializable {
    @Column(name = "excercise_id")
    private Long excerciseId;

    @Column(name = "user_id")
    private Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubmitId submitId = (SubmitId) o;
        return excerciseId.equals(submitId.excerciseId) && userId.equals(submitId.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(excerciseId, userId);
    }
}
