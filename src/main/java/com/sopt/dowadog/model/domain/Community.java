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
public class Community extends DateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    @Column(columnDefinition = "TEXT")
    private String detail;

}
