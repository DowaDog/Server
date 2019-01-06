package com.sopt.dowadog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailboxDto {

    private String title;
    private String detail;
    private String type;
    private String imgPath;
    private boolean complete;
}
