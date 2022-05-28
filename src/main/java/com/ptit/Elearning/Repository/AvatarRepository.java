package com.ptit.Elearning.Repository;

import com.ptit.Elearning.Entity.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AvatarRepository extends JpaRepository<Avatar,Long> {
    public Optional<Avatar> findByAvatarId(Long avatarId);
}
