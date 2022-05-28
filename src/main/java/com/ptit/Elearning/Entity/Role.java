package com.ptit.Elearning.Entity;

import com.ptit.Elearning.Constant.ERole;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "role")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int roleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name",length = 20)
    private ERole roleName;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "account_role",joinColumns = @JoinColumn(name = "role_id"),inverseJoinColumns = @JoinColumn(name = "account_id"))
    private Set<Account> accounts = new HashSet<>();
}
