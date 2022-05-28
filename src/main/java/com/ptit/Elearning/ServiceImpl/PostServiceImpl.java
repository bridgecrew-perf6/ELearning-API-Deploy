package com.ptit.Elearning.ServiceImpl;

import com.ptit.Elearning.Entity.CreditClass;
import com.ptit.Elearning.Entity.Post;
import com.ptit.Elearning.Exception.NotFoundException;
import com.ptit.Elearning.Repository.PostRepository;
import com.ptit.Elearning.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    PostRepository postRepository;
    @Override
    public Page<Post> getTopFivePostsCurrently(CreditClass creditClass, int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortField).ascending() : Sort.by(sortField).descending() ;

        Pageable pageable = PageRequest.of(pageNo -1, pageSize,sort);

        return postRepository.findByCreditClass(creditClass,pageable);
    }

    @Override
    public List<Post> getTopFivePostInCreditClass(CreditClass creditClass) {
        return   postRepository.findTop5ByCreditClassOrderByPostIdDesc(creditClass);
    }

    @Override
    public Post getPostById(long postId) {
        return postRepository.findByPostId(postId).orElseThrow(()->new NotFoundException("Could not find with "+postId));
    }

    @Override
    public Post createNewPostOrUpdate(Post post) {
        return postRepository.save(post);
    }


}
