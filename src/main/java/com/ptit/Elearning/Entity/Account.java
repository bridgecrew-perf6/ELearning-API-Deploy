package com.ptit.Elearning.Entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "account")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Account {

    public Account(String username,String password,UserInfo userInfo){
        this.username = username;
        this.password = password;
        this.userInfo = userInfo;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;


    private String username;
    private String password;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserInfo userInfo;

    @ManyToMany
    @JoinTable(name = "account_role",joinColumns = @JoinColumn(name = "account_id"),inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
}
