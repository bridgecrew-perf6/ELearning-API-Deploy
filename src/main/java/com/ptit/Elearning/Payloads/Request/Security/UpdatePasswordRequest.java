package com.ptit.Elearning.Payloads.Request.Security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdatePasswordRequest {
    @NotBlank
    @Size(min = 3,max = 20)
    private String username;

    @NotBlank
    @Size(min = 6,max = 40)
    private String password;
}
