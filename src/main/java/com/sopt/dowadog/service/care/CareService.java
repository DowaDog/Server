package com.sopt.dowadog.service.care;

import com.sopt.dowadog.model.domain.Care;
import com.sopt.dowadog.repository.CareRepository;
import com.sopt.dowadog.service.common.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CareService {
    @Autowired
    CareRepository careRepository;

    @Autowired
    JwtService jwtService;

    public Care getCareByJwtToken(String jwtToken) throws Exception {

        String userId = jwtService.decode(jwtToken);

        if(userId == null) return null;

        if(careRepository.findByCareUserId(userId).isPresent()) return careRepository.findByCareUserId(userId).get();

        return null;
    }


}
