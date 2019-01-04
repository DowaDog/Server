package com.sopt.dowadog.service;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.Registration;
import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.repository.RegistrationRepository;
import com.sopt.dowadog.util.ResponseMessage;
import com.sopt.dowadog.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class RegistrationService {

    @Autowired
    RegistrationRepository registrationRepository;

    private final String REG_STATUS_DENY = "deny";
    private final String REG_STATUS_STEP0 = "step0";
    private final String REG_STATUS_STEP1 = "step1";
    private final String REG_STATUS_STEP2 = "step2";
    private final String REG_STATUS_COMPLETE = "complete";

    private final String PROCESS_NOTICE = "notice";
    private final String PROCESS_ING = "ing";
    private final String PROCESS_ADOPT = "adopt";


    @Transactional
    public DefaultRes createRegistration(User user, Registration registration) {

        // 이부분 제대로 돌아가는지 확인 필요
        boolean validRequest = registrationRepository.existsByUserAndRegStatusNotAndValidReg(user, REG_STATUS_COMPLETE, false);
        System.out.println(validRequest);
        if(validRequest) return DefaultRes.BAD_REQUEST;

         //todo 여기 교육 다들었는지에대한 검증

        System.out.println(registration.getAnimal().toString());

        System.out.println(registration.getAnimal().getProcessState().toString());

        boolean validAnimal = registration.getAnimal().getProcessState().equals(PROCESS_NOTICE);

        System.out.println("step2");
        if(validAnimal) return DefaultRes.BAD_REQUEST;

        for(Registration lastRegistration : user.getRegistrationList()) {

            System.out.println("!!");
            lastRegistration.setUserCheck(true);
            registrationRepository.save(lastRegistration); // 지난 신청서리스트들 읽은상태로 변경
        }

        registrationRepository.save(registration);

        return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATE_REGISTRATION);
    }

}
