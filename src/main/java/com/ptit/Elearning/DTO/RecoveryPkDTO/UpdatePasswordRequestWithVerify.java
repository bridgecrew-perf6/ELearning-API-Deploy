package com.ptit.Elearning.DTO.RecoveryPkDTO;

import javax.validation.constraints.Size;

public class UpdatePasswordRequestWithVerify {
    @Size(min = 1)
    private String key;

    @Size(min = 1)
    private String codeValue;


    @Size(min = 6, max = 40)
    private String password;

    public UpdatePasswordRequestWithVerify() {
    }

    public UpdatePasswordRequestWithVerify(String key, String password,String codeValue) {
        this.key = key;
        this.password = password;
        this.codeValue = codeValue;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }
}
