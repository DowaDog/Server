package com.sopt.dowadog.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MainDto {

    private boolean login = false;
    private boolean userCheck = true;

    private String place = null;
    private String time = null;
    private String material = null;

    private String view;

}
