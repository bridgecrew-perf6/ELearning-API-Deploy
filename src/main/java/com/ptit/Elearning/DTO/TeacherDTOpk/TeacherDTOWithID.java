package com.ptit.Elearning.DTO.TeacherDTOpk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TeacherDTOWithID {
    private String fullname;
    private String phone;
    private String email;
    private String teacherId;
}
