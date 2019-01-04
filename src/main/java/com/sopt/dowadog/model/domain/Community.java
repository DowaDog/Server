package com.sopt.dowadog.model.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sopt.dowadog.model.domain.auditing.DateEntity;
import com.sopt.dowadog.model.dto.CommunityDto;
import lombok.*;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value={"hibernateLazyInitializer", "handler"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope=Community.class)
public class Community extends DateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    @Column(columnDefinition = "TEXT")
    private String detail;

    @OneToMany(mappedBy="community", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CommunityImg> communityImgList;

    @JsonIgnore
    @ManyToOne
    private User user;

    @Transient
    @JsonIgnore
    private List<MultipartFile> communityImgFiles;

    public boolean isToday() {
        Date createdAt = this.getCreatedAt();
        return (new DateTime(createdAt).toLocalDate()).equals(new LocalDate());
    }

    public boolean getAuth(String userId){
        return userId.equals(this.user.getId());
    }

    @JsonIgnore
    public CommunityDto getCommunityDto() {
        return CommunityDto.builder()
                .id(this.id)
                .title(this.title)
                .detail(this.detail)
                .communityImgList(this.communityImgList)
                .userId(this.user.getId())
                .today(this.isToday())
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .build();
    }



}
