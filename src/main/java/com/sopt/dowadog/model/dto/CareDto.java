package com.sopt.dowadog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CareDto {

    private String region;
    private String address;
    private String name;
    private String tel;

}
