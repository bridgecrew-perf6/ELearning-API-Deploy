package com.ptit.Elearning.DTO.SubmitDTOpk;

import com.ptit.Elearning.Entity.Document;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubmitResponseDTO {
    private Long excerciseId;
    private Long userId;
    private String studentCode;
    private String fullname;
    private String submitContent;
    private String documentURL;
    private Timestamp submitTime;
    private float mark;
    private Long avatarId;

    @Override
    public String toString() {
        return "SubmitResponseDTO{" +
                "excerciseId=" + excerciseId +
                ", userId=" + userId +
                ", studentCode='" + studentCode + '\'' +
                ", fullname='" + fullname + '\'' +
                ", submitContent='" + submitContent + '\'' +
                ", documentId=" + documentURL +
                ", submitTime=" + submitTime +
                ", mark=" + mark +
                ", avatarId=" + avatarId +
                '}';
    }
}
