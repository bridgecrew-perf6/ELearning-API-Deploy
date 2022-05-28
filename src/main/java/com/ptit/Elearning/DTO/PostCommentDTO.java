package com.ptit.Elearning.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostCommentDTO {
    private Long userId;
    private String userName;
    private String avatar;
    private long commentId;
    private String content;
    private Timestamp createdAt;
    private boolean isTeacher;
    private String code;
}
