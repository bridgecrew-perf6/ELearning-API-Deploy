package com.ptit.Elearning.Payloads.Request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

//    @NotBlank
//    private Long userId;

    private Set<String> role;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @Size(min = 6)
    private String key;

    @Size(min = 6)
    private String codeValue;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

//    public Long getUserId(){
//        return this.userId;
//    }
//    public void setUserId(Long userId){
//        this.userId = userId;
//    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRole() {
        return this.role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }
}
