package com.ptit.Elearning.DTO.PostDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostDTO {
    private long postId;
    private String avartarPublisher;
    private String fullname;
    private String creditClassId;
    private String subjectName;
    private String postedTime;
    private String postContent;
}
