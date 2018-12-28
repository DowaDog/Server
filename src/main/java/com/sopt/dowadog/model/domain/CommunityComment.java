package com.sopt.dowadog.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sopt.dowadog.model.domain.auditing.DateEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    private Community community;

}
