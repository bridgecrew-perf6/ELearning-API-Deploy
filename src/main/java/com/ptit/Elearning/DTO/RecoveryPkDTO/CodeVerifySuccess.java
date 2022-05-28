package com.ptit.Elearning.DTO.RecoveryPkDTO;

import javax.swing.*;

public class CodeVerifySuccess {
    String valueKey ;
    String codeValue;
    public CodeVerifySuccess() {
    }

    public CodeVerifySuccess(String valueKey, String key2) {
        this.valueKey = valueKey;
        this.codeValue = key2;
    }

    public String getValueKey() {
        return valueKey;
    }

    public void setValueKey(String valueKey) {
        this.valueKey = valueKey;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }
}
