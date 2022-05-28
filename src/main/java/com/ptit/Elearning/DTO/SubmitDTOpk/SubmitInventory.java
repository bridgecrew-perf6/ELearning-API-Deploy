package com.ptit.Elearning.DTO.SubmitDTOpk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SubmitInventory {
    private float high; //từ 8 trở lên
    private float medium; // từ 6.5 đến 8
    private float low; //từ 5-> 6.5
    private float veryLow; //dưới 5
}
