package com.sopt.dowadog.model.dto;

import com.sopt.dowadog.model.domain.CommunityImg;
import com.sopt.dowadog.model.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityDto {
    private int id;
    private String title;
    private String detail;
    private List<CommunityImg> communityImgList;
    private boolean today;
    private String userId;
    private String userProfileImg;
    private Date createdAt;
    private Date updatedAt;
    private boolean auth = false;

}
