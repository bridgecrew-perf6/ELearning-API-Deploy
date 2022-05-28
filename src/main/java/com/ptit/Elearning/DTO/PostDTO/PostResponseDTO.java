package com.ptit.Elearning.DTO.PostDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostResponseDTO {
    private Long postId;
    private String postContent;
    private Timestamp createdAt;
    private int status;
}
