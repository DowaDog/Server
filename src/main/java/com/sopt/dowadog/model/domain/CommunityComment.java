package com.sopt.dowadog.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sopt.dowadog.model.domain.auditing.DateEntity;
import com.sopt.dowadog.model.dto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value={"hibernateLazyInitializer", "handler"})
public class CommunityComment extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "TEXT")
    private String detail;

    @JsonIgnore
    @ManyToOne
    private Community community;

    @OneToOne
    @JsonIgnore
    private User user;

    public boolean isToday() {
        Date createdAt = this.getCreatedAt();
        return (new DateTime(createdAt).toLocalDate()).equals(new LocalDate());
    }

    public String getUserId() {
        return this.user.getId();
    }

    public boolean getAuth(String userId){

        return userId.equals(this.user.getId());
    }

    @JsonIgnore
    public CommentDto getCommentDto() {
        return CommentDto.builder()
                .id(this.id)
                .detail(this.detail)
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .userId(this.getUserId())
                .build();
    }



}
