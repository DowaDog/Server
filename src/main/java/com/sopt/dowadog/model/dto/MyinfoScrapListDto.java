package com.sopt.dowadog.model.dto;

import lombok.*;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyinfoScrapListDto {
    private int id;
    private String title;
    private LocalDateTime createdAt;

}
