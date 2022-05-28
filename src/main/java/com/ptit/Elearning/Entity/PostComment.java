package com.ptit.Elearning.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "post_comment")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserInfo userInfo;

    private String content;

    @Column(name = "created_at")
    private Timestamp createdAt;

    private int status; //0: đã bị xóa, 1: hiện tại vẫn còn.
}
