package com.sopt.dowadog.service.common;

import com.sopt.dowadog.model.domain.Code;
import com.sopt.dowadog.repository.CodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CodeService {

    @Autowired
    CodeRepository codeRepository;

    public List<Code> readCodeByCodeGroup(String codeGroup) {
        return codeRepository.findByCodeGroup(codeGroup);

    }
}
