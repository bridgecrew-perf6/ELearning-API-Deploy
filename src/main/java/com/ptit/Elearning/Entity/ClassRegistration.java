package com.ptit.Elearning.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "class_registration")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClassRegistration {
    @EmbeddedId
    private ClassRegistrationId classRegistrationId;
    private int status; //1: joined; 2: invited; 3: no longer joined.

    @Column(name = "joined_time")
    private Date joinedTime;

    @ManyToOne
    @JoinColumn(name = "user_id",insertable = false,updatable = false)
    private UserInfo userInfo;

    @ManyToOne
    @JoinColumn(name = "credit_class_id",insertable = false,updatable = false)
    private CreditClass creditClass;
}
