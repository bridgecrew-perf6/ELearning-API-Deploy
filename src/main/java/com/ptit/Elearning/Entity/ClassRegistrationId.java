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
public class ClassRegistrationId implements Serializable {
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "credit_class_id")
    private Long creditClassId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassRegistrationId that = (ClassRegistrationId) o;
        return userId.equals(that.userId) && creditClassId.equals(that.creditClassId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, creditClassId);
    }
}
