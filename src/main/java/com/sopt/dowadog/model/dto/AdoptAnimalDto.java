package com.sopt.dowadog.model.dto;

import com.sopt.dowadog.model.domain.AnimalUserAdopt;
import com.sopt.dowadog.model.domain.Code;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdoptAnimalDto {
    private AnimalUserAdopt animalUserAdopt;
    private List<InoculationCode> inoculationList;
}
