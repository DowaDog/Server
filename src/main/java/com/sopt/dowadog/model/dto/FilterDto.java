package com.sopt.dowadog.model.dto;

import lombok.Data;
import java.util.Date;

@Data
public class FilterDto {
    private String type;
    private Integer remainNoticeDate;
    private String region;
    private String tag;
    private boolean story;
    private String searchWord;
}
