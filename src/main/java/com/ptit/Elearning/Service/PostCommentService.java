package com.ptit.Elearning.Service;

import com.ptit.Elearning.Entity.Post;
import com.ptit.Elearning.Entity.PostComment;

import java.util.List;

public interface PostCommentService {
    public List<PostComment> getAllCommentsByPost(Post post);
    public PostComment getById(Long commentId);
    public boolean deleteComment(PostComment postComment);
    public PostComment addComment(PostComment postComment);
}
