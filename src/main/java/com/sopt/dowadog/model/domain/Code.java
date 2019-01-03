package com.sopt.dowadog.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sopt.dowadog.model.dto.InoculationCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Code {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String codeGroup;

    private String code;

    private String codeName;

    @JsonIgnore
    public InoculationCode getCodeDto(){
        return InoculationCode.builder()
                .code(this.code)
                .codeName(this.codeName)
                .build();
    }

}
