package com.sopt.dowadog.service.care;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.*;
import com.sopt.dowadog.model.dto.RegistrationDto;
import com.sopt.dowadog.model.dto.RegistrationMaterialDto;
import com.sopt.dowadog.repository.AnimalRepository;
import com.sopt.dowadog.repository.AnimalUserAdoptRepository;
import com.sopt.dowadog.repository.CareRepository;
import com.sopt.dowadog.repository.RegistrationRepository;
import com.sopt.dowadog.util.ResponseMessage;
import com.sopt.dowadog.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CareRegistrationService {

    @Autowired
    RegistrationRepository registrationRepository;
    @Autowired
    CareRepository careRepository;
    @Autowired
    AnimalRepository animalRepository;
    @Autowired
    AnimalUserAdoptRepository animalUserAdoptRepository;


    private final String REG_STATUS_DENY = "deny";
    private final String REG_STATUS_STEP0 = "step0";
    private final String REG_STATUS_STEP1 = "step1";
    private final String REG_STATUS_STEP2 = "step2";
    private final String REG_STATUS_STEP3 = "step3";
    private final String REG_STATUS_COMPLETE = "complete";

    private final String PROCESS_NOTICE = "notice";
    private final String PROCESS_STEP = "step";
    private final String PROCESS_TEMP = "temp";
    private final String PROCESS_ADOPT = "adopt";
    private final String PROCESS_END = "end";

    public DefaultRes<RegistrationDto> readRegistrationById(Care care, int registrationId) {
        if(!registrationRepository.findById(registrationId).isPresent()) return DefaultRes.NOT_FOUND;

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTRATION, registrationRepository.findById(registrationId).get().getRegistrationDto());
    }

    //STEP0 사용자가 신청
    @Transactional
    public DefaultRes updateRegistrationAcceptStepZero(Care care, int registrationId) {

        System.out.println("스텝0 요청 수락");

        //검증
        if (!registrationRepository.findById(registrationId).isPresent()) return DefaultRes.BAD_REQUEST;
        Registration registration = registrationRepository.findById(registrationId).get();
        Animal animal = registration.getAnimal();

        if (!checkStepZeroRequest(care, animal, registration)) return DefaultRes.BAD_REQUEST;
        //검증 끝

        //비즈니스로직
        registration.setRegStatus(REG_STATUS_STEP1);
        registration.setUserCheck(false);
        animal.setProcessState(PROCESS_STEP);
        //todo 우체통 생성, 푸시생성

        registrationRepository.save(registration);
        animalRepository.save(animal);

        return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_REGISTRATION);
    }

    //STEP0 반려
    @Transactional
    public DefaultRes updateRegistrationDenyStepZero(Care care, int registrationId) {
        System.out.println("스텝0 요청 거절");


        //검증
        if (!registrationRepository.findById(registrationId).isPresent()) return DefaultRes.BAD_REQUEST;
        Registration registration = registrationRepository.findById(registrationId).get();
        Animal animal = registration.getAnimal();

        if (!checkStepZeroRequest(care, animal, registration)) return DefaultRes.BAD_REQUEST;
        //검증 끝

        //비즈니스로직
        registration.setRegStatus(REG_STATUS_DENY);
        registration.setValidReg(false);
        registration.setUserCheck(false);
        //todo 우체통 생성, 푸시생성

        return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_REGISTRATION);
    }

    private boolean checkStepZeroRequest(Care care, Animal animal, Registration registration) {

        if (!registration.getAnimal().getCare().equals(care)) {
            return false;
        }

        if (!care.getAnimalList().contains(animal)) {
            System.out.println("해당 동물 없음");
            return false;
        }

        if (!animal.getProcessState().equals(PROCESS_NOTICE)) {
            System.out.println("동물 공고중 아님");
            return false;
        }

        if (!registration.getRegStatus().equals(REG_STATUS_STEP0)) {
            System.out.println("STEP0 단계가 아님");
            return false;
        }
        return true;
    }





    //STEP1 수락   전화받은 후 수락 대기
    @Transactional
    public DefaultRes updateRegistrationAcceptStepOne(Care care, RegistrationMaterialDto registrationMaterialDto, int registrationId) {
        System.out.println("스텝1 요청 수락");

        //검증
        if (!registrationRepository.findById(registrationId).isPresent()) return DefaultRes.BAD_REQUEST;
        Registration registration = registrationRepository.findById(registrationId).get();
        Animal animal = registration.getAnimal();

        if (!checkStepOneRequest(care, animal, registration)) return DefaultRes.BAD_REQUEST;
        //검증 끝

        //비즈니스로직
        registration.setRegStatus(REG_STATUS_STEP2);
        registration.setUserCheck(false);
        registration.setMeetTime(registrationMaterialDto.getMeetTime());
        registration.setMeetPlace(registrationMaterialDto.getMeetPlace());
        registration.setMeetMaterial(registrationMaterialDto.getMeetMaterial());
        //todo 우체통 생성, 푸시생성

        registrationRepository.save(registration);
        animalRepository.save(animal);

        return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_REGISTRATION);
    }

    //STEP1 반려
    @Transactional
    public DefaultRes updateRegistrationDenyStepOne(Care care, int registrationId) {
        System.out.println("스텝1 요청 거절");
        //검증
        if (!registrationRepository.findById(registrationId).isPresent()) return DefaultRes.BAD_REQUEST;
        Registration registration = registrationRepository.findById(registrationId).get();
        Animal animal = registration.getAnimal();

        if (!checkStepOneRequest(care, animal, registration)) return DefaultRes.BAD_REQUEST;
        //검증 끝

        //비즈니스로직
        registration.setRegStatus(REG_STATUS_DENY);
        registration.setValidReg(false);
        registration.setUserCheck(false);
        animal.setProcessState(PROCESS_NOTICE);
        //todo 우체통 생성, 푸시생성

        registrationRepository.save(registration);
        animalRepository.save(animal);

        return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_REGISTRATION);
    }


    private boolean checkStepOneRequest(Care care, Animal animal, Registration registration) {

        if (!registration.getAnimal().getCare().equals(care)) {
            return false;
        }

        if (!care.getAnimalList().contains(animal)) {
            System.out.println("해당 동물 없음");
            return false;
        }

        if (!animal.getProcessState().equals(PROCESS_STEP)) {
            System.out.println("진행중 동물 아님");
            return false;
        }

        if (!registration.getRegStatus().equals(REG_STATUS_STEP1)) {
            System.out.println("STEP1 단계가 아님");
            return false;
        }
        return true;
    }




    //STEP2 수락   전화받은 후 수락 대기
    @Transactional
    public DefaultRes updateRegistrationAcceptStepTwo(Care care, int registrationId) {
        System.out.println("스텝1 요청 수락");

        //검증
        if (!registrationRepository.findById(registrationId).isPresent()) return DefaultRes.BAD_REQUEST;
        Registration registration = registrationRepository.findById(registrationId).get();
        Animal animal = registration.getAnimal();

        if (!checkStepTwoRequest(care, animal, registration)) return DefaultRes.BAD_REQUEST;
        //검증 끝

        //비즈니스로직
        registration.setRegStatus(REG_STATUS_STEP3);
        registration.setUserCheck(false);
        //todo 우체통 생성, 푸시생성

        registrationRepository.save(registration);
        animalRepository.save(animal);

        return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_REGISTRATION);
    }

    //STEP2 반려
    @Transactional
    public DefaultRes updateRegistrationDenyStepTwo(Care care, int registrationId) {
        System.out.println("스텝1 요청 거절");
        //검증
        if (!registrationRepository.findById(registrationId).isPresent()) return DefaultRes.BAD_REQUEST;
        Registration registration = registrationRepository.findById(registrationId).get();
        Animal animal = registration.getAnimal();

        if (!checkStepTwoRequest(care, animal, registration)) return DefaultRes.BAD_REQUEST;
        //검증 끝

        //비즈니스로직
        registration.setRegStatus(REG_STATUS_DENY);
        registration.setValidReg(false);
        registration.setUserCheck(false);
        animal.setProcessState(PROCESS_NOTICE);
        //todo 우체통 생성, 푸시생성

        registrationRepository.save(registration);
        animalRepository.save(animal);

        return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_REGISTRATION);
    }


    private boolean checkStepTwoRequest(Care care, Animal animal, Registration registration) {

        if (!registration.getAnimal().getCare().equals(care)) {
            return false;
        }

        if (!care.getAnimalList().contains(animal)) {
            System.out.println("해당 동물 없음");
            return false;
        }

        if (!animal.getProcessState().equals(PROCESS_STEP)) {
            System.out.println("진행중 동물 아님");
            return false;
        }

        if (!registration.getRegStatus().equals(REG_STATUS_STEP2)) {
            System.out.println("STEP2 단계가 아님");
            return false;
        }
        return true;
    }


    //Complete 수락
    @Transactional
    public DefaultRes updateRegistrationAcceptComplete(Care care, int registrationId) {
        System.out.println("입양완료 요청 수락");

        //검증
        if (!registrationRepository.findById(registrationId).isPresent()) return DefaultRes.BAD_REQUEST;
        Registration registration = registrationRepository.findById(registrationId).get();
        Animal animal = registration.getAnimal();

        if (!checkAcceptFinalRequest(care, animal, registration)) return DefaultRes.BAD_REQUEST;
        //검증 끝

        //비즈니스로직
        registration.setRegStatus(REG_STATUS_COMPLETE);
        registration.setValidReg(false);
        registration.setUserCheck(false);
        animal.setProcessState(PROCESS_ADOPT);

        User user = registration.getUser();

        AnimalUserAdopt animalUserAdopt = AnimalUserAdopt.builder()
                                            .user(user)
                                            .name(new StringBuilder(user.getName()).append("펫").toString())
                                            .gender(animal.getSexCd())
                                            .kind(animal.getKindCd())
                                            .age(animal.getAge())
                                            .weight(animal.getWeight())
                                            .neuterYn(animal.getNeuterYn())
                                            .registration(registration)
                                            .profileImg(animal.getThumbnailImg())
                                            .build();


        //todo 우체통 생성, 푸시생성

        registrationRepository.save(registration);
        animalRepository.save(animal);
        animalUserAdoptRepository.save(animalUserAdopt);


        return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_REGISTRATION);
    }


    //Complete 반려
    @Transactional
    public DefaultRes updateRegistrationDenyComplete(Care care, int registrationId) {
        System.out.println("입양완료 요청 거절");
        //검증
        if (!registrationRepository.findById(registrationId).isPresent()) return DefaultRes.BAD_REQUEST;
        Registration registration = registrationRepository.findById(registrationId).get();
        Animal animal = registration.getAnimal();

        if (!checkAcceptFinalRequest(care, animal, registration)) return DefaultRes.BAD_REQUEST;
        //검증 끝

        //비즈니스로직
        registration.setRegStatus(REG_STATUS_DENY);
        registration.setValidReg(false);
        registration.setUserCheck(false);
        animal.setProcessState(PROCESS_NOTICE);
        //todo 우체통 생성, 푸시생성

        registrationRepository.save(registration);
        animalRepository.save(animal);

        return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_REGISTRATION);
    }

    private boolean checkAcceptFinalRequest(Care care, Animal animal, Registration registration) {

        if (!registration.getAnimal().getCare().equals(care)) {
            return false;
        }

        if (!care.getAnimalList().contains(animal)) {
            System.out.println("해당 동물 없음");
            return false;
        }

        if (!animal.getProcessState().equals(PROCESS_STEP)) {
            System.out.println("진행중 동물 아님");
            return false;
        }

        if (!registration.getRegStatus().equals(REG_STATUS_STEP3)) {
            System.out.println("STEP3 단계가 아님");
            return false;
        }
        return true;
    }


}
