package com.ptit.Elearning.Repository;

import com.ptit.Elearning.Constant.ERole;
import com.ptit.Elearning.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
    Optional<Role> findByRoleName(ERole name);
}
