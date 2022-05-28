package com.ptit.Elearning.ServiceImpl;

import com.ptit.Elearning.Entity.Folder;
import com.ptit.Elearning.Entity.Post;
import com.ptit.Elearning.Entity.PostComment;
import com.ptit.Elearning.Exception.NotFoundException;
import com.ptit.Elearning.Repository.PostCommentRepository;
import com.ptit.Elearning.Repository.PostRepository;
import com.ptit.Elearning.Service.PostCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostCommentServiceImpl implements PostCommentService{


    @Autowired
    PostCommentRepository postCommentRepository;

    @Autowired
    PostRepository postRepository;

    @Override
    public List<PostComment> getAllCommentsByPost(Post post) {
        return postCommentRepository.findByPostAndStatusOrderByCommentIdAsc(post,1);
    }

    @Override
    public PostComment getById(Long commentId) {
        return postCommentRepository.findByCommentId(commentId).orElseThrow(()-> new NotFoundException("Could not find comment"));
    }

    @Override
    public boolean deleteComment(PostComment postComment) {
        try{
            Post post = postComment.getPost();
            post.getComments().removeIf(p->p.getCommentId()==postComment.getCommentId());
            postRepository.save(post);

            //vẫn là không thể thực hiện câu lệnh xóa này được
            postCommentRepository.delete(postComment);
        }catch (Exception e){
            return false;
        }
        return true;


    }

    @Override
    public PostComment addComment(PostComment postComment) {
        return postCommentRepository.save(postComment);
    }
}
