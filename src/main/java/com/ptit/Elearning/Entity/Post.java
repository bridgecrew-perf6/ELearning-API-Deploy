package com.ptit.Elearning.Entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "post")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "post_content",length = 500)
    private String postContent;

    @Column(name = "created_at")
    private Timestamp createdAt;

    private int status;

    @ManyToOne
    @JoinColumn(name = "credit_class_id")
    private CreditClass creditClass;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserInfo userInfo;

    @OneToMany(mappedBy = "post",fetch = FetchType.EAGER,cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<PostComment> comments = new HashSet<>();
}
