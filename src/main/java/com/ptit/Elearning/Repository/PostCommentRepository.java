package com.ptit.Elearning.Repository;

import com.ptit.Elearning.Entity.Post;
import com.ptit.Elearning.Entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment,Long> {
    public List<PostComment> findByPostAndStatusOrderByCommentIdAsc(Post post,int status);
    public Optional<PostComment> findByCommentId(Long commentId);
}
