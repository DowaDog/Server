package com.sopt.dowadog.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MainDto {

    private boolean login = false;
    private boolean adopting = false;

    private boolean userCheck;
    private boolean todayAdopt;

    private Boolean registrationUpdated = null;
    private Boolean mailboxUpdated = null;
    private String regStatus = null; // step1, step2, step3, step4, step5

    //on step3
    private String place = null;
    private String time = null;
    private String material = null;

}
