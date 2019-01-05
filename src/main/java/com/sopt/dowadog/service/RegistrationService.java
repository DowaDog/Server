package com.sopt.dowadog.service;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.Animal;
import com.sopt.dowadog.model.domain.Registration;
import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.model.dto.AllEducatedDto;
import com.sopt.dowadog.model.dto.RegistrationDto;
import com.sopt.dowadog.repository.AnimalRepository;
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

    @Autowired
    AnimalRepository animalRepository;

    @Autowired
    CardnewsService cardnewsService;

    private final String REG_STATUS_DENY = "deny";
    private final String REG_STATUS_STEP0 = "step0";
    private final String REG_STATUS_STEP1 = "step1";
    private final String REG_STATUS_STEP2 = "step2";
    private final String REG_STATUS_COMPLETE = "complete";

    private final String PROCESS_NOTICE = "notice";
    private final String PROCESS_STEP = "step";
    private final String PROCESS_TEMP = "temp";
    private final String PROCESS_ADOPT = "adopt";
    private final String PROCESS_END = "end";



    @Transactional
    public DefaultRes createRegistration(User user, RegistrationDto registrationDto, String regType) {
        Animal animal = animalRepository.findById(registrationDto.getAnimalId()).get();
        //검증 부분
        if(!checkRequest(user, animal, registrationDto)) return DefaultRes.BAD_REQUEST;

        //검증 끝
        Registration registration = Registration.getRegistrationByDto(registrationDto);
        registration.setUser(user);
        registration.setAnimal(animal);
        registration.setRegType(regType);
        registration.setRegStatus("step0");

        registrationRepository.save(registration);
        return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATE_REGISTRATION);
    }


    private boolean checkRequest(User user, Animal animal, RegistrationDto registrationDto) {
        // 유저가 가진 신청서중에 complete를 상태를 제외한것중에 valid가 true(현재 진행중인 신청서가 있는지)인것이 있는지 체크
        boolean abuser = registrationRepository.existsByUserAndRegStatusNotInAndValidReg(user, REG_STATUS_COMPLETE, true);
        if(abuser) return false;
        System.out.println("유저 신청서 상태 검증 완료");


        //모든 교육 마치지 않았다면 //Todo 올바른 코드로 변경되면 그때 적용
//        AllEducatedDto allEducatedDto = cardnewsService.getAllEducatedDtoComplete(user);
//        if(!allEducatedDto.isAllComplete()) return DefaultRes.BAD_REQUEST;

        //없는 동물에 대해 신청하면
        if(!animalRepository.findById(registrationDto.getAnimalId()).isPresent()) return false;
        System.out.println("존재하는 동물에 대한 요청인지 검증 완료");


        //공고중이 아닌것에 대해서 //todo 임보가 입양가능상태로 들어가면 여기 로직 바뀌어야함
        if(!animal.getProcessState().equals(PROCESS_NOTICE)) return false;
        System.out.println("공고중인 동물에 대한 요청인지 검증 완료");

        //이전의 모든 신청서에 읽음 처리
        if(registrationRepository.findByUserAndUserCheck(user, false).isPresent()) {
            System.out.println("이전 신청서들 읽었음으로 설정");
            for (Registration lastRegistration : registrationRepository.findByUserAndUserCheck(user, false).get()) {
                lastRegistration.setUserCheck(true);
                registrationRepository.save(lastRegistration); // 지난 신청서리스트들 읽은상태로 변경
            }
        }
        return true;
    }

}
