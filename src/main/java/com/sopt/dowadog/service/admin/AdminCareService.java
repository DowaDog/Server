package com.sopt.dowadog.service.admin;

import com.sopt.dowadog.model.domain.Care;
import com.sopt.dowadog.repository.CareRepository;
import com.sopt.dowadog.util.SHA256Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kohen.kang on 2019-01-04..
 */

@Service
public class AdminCareService {

    @Autowired
    CareRepository careRepository;
    @Autowired
    SHA256Util sha256Util;


    public void createCare(Care care) {

        String carePassword = care.getPassword();
        care.setPassword(sha256Util.SHA256Util(carePassword));

        careRepository.save(care);
    }

    public List<Care> readAllCare() {
        return careRepository.findAll();
    }
}
