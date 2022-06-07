package com.ptit.Elearning.DTO.CreditclassDTOpk;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreditClassRequest {
    @NotBlank
    private String startTime;

    @NotBlank
    private String endTime;

    @NotBlank
    private String schoolYear;

//    @NotBlank
    private int status;

    @NotBlank
    @Size(min = 3,max = 30)
    private String joinedPassword;

//    @NotNull
    private int departmentId;

//    @NotEmpty
    private int subjectId;

    @NotNull
    private List<String> teacherId;

}
