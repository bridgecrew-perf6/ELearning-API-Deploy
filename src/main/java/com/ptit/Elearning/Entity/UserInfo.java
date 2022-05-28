package com.ptit.Elearning.Entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_info")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;


    private String address;
    private boolean gender;

    @Column(unique = true)
    private String email;

    @Column(length = 10,unique = true)
    private String phone;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @OneToOne
    @JoinColumn(name = "avatar_id")
    private Avatar avatar;

    private String fullname;
    private boolean active;
}
