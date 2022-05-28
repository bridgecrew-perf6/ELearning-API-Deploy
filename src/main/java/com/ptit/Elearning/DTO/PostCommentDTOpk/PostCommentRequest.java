package com.ptit.Elearning.DTO.PostCommentDTOpk;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PostCommentRequest {

    private Long postId;

    @NotBlank
    @Size(max = 200)
    private String content;

    public PostCommentRequest() {
    }

    public PostCommentRequest(Long postId, String content) {
        this.postId = postId;
        this.content = content;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
