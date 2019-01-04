package com.sopt.dowadog.service.admin;

import com.sopt.dowadog.model.domain.Care;
import com.sopt.dowadog.repository.CareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kohen.kang on 2019-01-04..
 */

@Service
public class AdminCareService {

    @Autowired
    CareRepository careRepository;

    public void createCare(Care care) {

        careRepository.save(care);
    }

    public List<Care> readAllCare() {
        return careRepository.findAll();
    }
}
