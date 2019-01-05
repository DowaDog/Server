package com.sopt.dowadog.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sopt.dowadog.model.domain.auditing.DateEntity;
import com.sopt.dowadog.model.dto.CardnewsContentsDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Entity
@JsonIgnoreProperties(value={"hibernateLazyInitializer", "handler"})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardnewsContents extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private String thumnailImg;

    @Transient
    @JsonIgnore
    private MultipartFile cardnewsContentsImgFile;

    @Column(columnDefinition = "TEXT")
    private String detail;

    @ManyToOne
    //@JoinColumn(name = "cardnews_id")
    private Cardnews cardnews;

    @JsonIgnore
    public CardnewsContentsDto getCardnewsContentsDto() {
        return CardnewsContentsDto.builder()
                .id(this.id)
                .title(this.title)
                .thumnailImg(this.thumnailImg)
                .detail(this.detail)
                .build();
    }
}
