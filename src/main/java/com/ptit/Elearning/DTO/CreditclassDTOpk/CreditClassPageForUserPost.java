package com.ptit.Elearning.DTO.CreditclassDTOpk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreditClassPageForUserPost {
    int totalPage;
    List<CreditClassDTOWithPostInfo> creditClassDTOS;
}
