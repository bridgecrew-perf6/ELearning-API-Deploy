package com.ptit.Elearning.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "verify_account")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerifyAccount {
    @Id
    @Column(name = "verify_id")
    UUID verifyId ;

    @Column
    String key1;

    @Column
    String key2;

    @OneToOne
    @JoinColumn(name = "user_id")
    UserInfo userInfo;


}
