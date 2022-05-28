package com.ptit.Elearning.Service;

import com.ptit.Elearning.Entity.CreditClass;
import com.ptit.Elearning.Entity.Post;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {
    public Page<Post> getTopFivePostsCurrently(CreditClass creditClass, int pageNo, int pageSize, String sortField, String sortDirection);
    public List<Post> getTopFivePostInCreditClass(CreditClass creditClass);
    public Post getPostById(long postId);
    public Post createNewPostOrUpdate(Post post);
}
