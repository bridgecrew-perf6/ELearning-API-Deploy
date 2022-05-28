package com.ptit.Elearning.Repository;

import com.ptit.Elearning.Entity.CreditClass;
import com.ptit.Elearning.Entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    public Page<Post> findByCreditClass(CreditClass creditClass, Pageable pageable);
    public List<Post> findTop5ByCreditClassOrderByPostIdDesc(CreditClass creditClass);
    public Optional<Post> findByPostId(long postId);

}
