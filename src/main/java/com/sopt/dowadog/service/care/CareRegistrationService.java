package com.sopt.dowadog.service.care;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.*;
import com.sopt.dowadog.model.dto.RegistrationDto;
import com.sopt.dowadog.model.dto.RegistrationMaterialDto;
import com.sopt.dowadog.repository.*;
import com.sopt.dowadog.util.AsyncUtil;
import com.sopt.dowadog.util.ResponseMessage;
import com.sopt.dowadog.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    MailboxRepository mailboxRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MailboxReservationRepository mailboxReservationRepository;

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





    //우체통 생성, 푸시알람 한 번에 처리하는 함수
    private boolean setMailAndAlram (User user, String title, String detail){
        try{
            System.out.println("---우체통 생성, 푸시알람 함수 들어옴---");
            //우체통 생성
            Mailbox mailbox = Mailbox.builder()
                    .type("registration")
                    .detail(detail)// 바꿔야함 (임의로)
                    .title(title)// 바꿔야함 (임의로)
                    .user(user)
                    .build();

            // 우체통에 접근
            mailboxRepository.save(mailbox);

            if(user.getDeviceToken()!=null){// 아요에스는 애초에 디바이스 토큰을 널로 주기 때문에 분기 처리
                //푸시 생성
                AsyncUtil asyncUtil = new AsyncUtil();
                System.out.println(user.getDeviceToken());

                return asyncUtil.sendOne(user.getDeviceToken(),title,detail).isDone();

            }else{

                return true;
            }


        }catch (Exception e){
            e.printStackTrace();
            return false;

        }

    }
    // 날짜 리스트 계산해서 배열에 넣는 함수(사진을 위한 데이트)
    private List<LocalDate> getDateList(){


        List<LocalDate> dateList = new ArrayList<>();
        LocalDate nowDate = LocalDate.now();
        LocalDate afterOneYear = nowDate.plusYears(1);
        System.out.println(afterOneYear);

        for(int i = 1 ; i<13; i++){
            dateList.add(nowDate.plusMonths(i));
        }
        // 날짜에 대해서 한 달에 한 번씩

        System.out.println(dateList.size());
        return dateList;
    }

    public List<Registration> readAnimalRegistrationByAnimalId(int animalId) {

        Animal animal = animalRepository.findById(animalId).get();

        List<Registration> registrationList = new ArrayList<>();

        if(registrationRepository.findByAnimal(animal).isPresent()){
            registrationList = registrationRepository.findByAnimal(animal).get();
        }

        return registrationList;

    }


    public DefaultRes<RegistrationDto> readRegistrationById(Care care, int registrationId) {
        if(!registrationRepository.findById(registrationId).isPresent()) return DefaultRes.NOT_FOUND;

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTRATION, registrationRepository.findById(registrationId).get().getRegistrationDto());
    }


    //STEP0 사용자가 신청
    @Transactional
    public DefaultRes updateRegistrationAcceptStepZero(Care care, int registrationId) {

        System.out.println("스텝0 요청 수락");

        //검증
        if (!registrationRepository.findById(registrationId).isPresent()){
            System.out.println("해당 신청서 없음");
            return DefaultRes.BAD_REQUEST;
        }
        Registration registration = registrationRepository.findById(registrationId).get();
        Animal animal = registration.getAnimal();

        if (!checkStepZeroRequest(care, animal, registration)) {
            System.out.println("해당 신청서 상태 검증 실패");

            return DefaultRes.BAD_REQUEST;
        }

        if (!animal.getProcessState().equals(PROCESS_NOTICE)) {
            System.out.println("동물 공고중 아님");
            return DefaultRes.BAD_REQUEST;
        }

        //검증 끝

        //비즈니스로직
        registration.setRegStatus(REG_STATUS_STEP1);
        registration.setUserCheck(false);
        animal.setProcessState(PROCESS_STEP);
        System.out.println("비즈니스 로직 실행완료");

        //todo 우체통 생성, 푸시생성

        // 유저 가져오기
        User user = registrationRepository.findById(registrationId).get().getUser();
        // 메세지 바꾸기
        setMailAndAlram(user,"스텝0 요청 수락","메일함을 확인해보세요");


        registrationRepository.save(registration);
        animalRepository.save(animal);
        System.out.println("flush완료");


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

        // 유저 가져오기
        User user = registrationRepository.findById(registrationId).get().getUser();
        // 메세지 바꾸기
        setMailAndAlram(user,"스텝0 요청 거절","메일함을 확인해보세요");

        return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_REGISTRATION);
    }

    private boolean checkStepZeroRequest(Care care, Animal animal, Registration registration) {

        if (!registration.getAnimal().getCare().equals(care)) {
            System.out.println("해당 보호소의 동물이 아님");
            return false;
        }

        if (!care.getAnimalList().contains(animal)) {
            System.out.println("해당 동물 없음");
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

        // 유저 가져오기
        User user = registrationRepository.findById(registrationId).get().getUser();
        // 메세지 바꾸기
        setMailAndAlram(user,"스텝1 요청 수락","메일함을 확인해보세요");

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
        // 유저 가져오기
        User user = registrationRepository.findById(registrationId).get().getUser();
        // 메세지 바꾸기
        setMailAndAlram(user,"스텝1 요청 거절","메일함을 확인해보세요");

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
        System.out.println("스텝2 요청 수락");

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
        // 유저 가져오기
        User user = registrationRepository.findById(registrationId).get().getUser();
        // 메세지 바꾸기
        setMailAndAlram(user,"스텝2 요청 수락","메일함을 확인해보세요");

        registrationRepository.save(registration);
        animalRepository.save(animal);

        return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_REGISTRATION);
    }

    //STEP2 반려
    @Transactional
    public DefaultRes updateRegistrationDenyStepTwo(Care care, int registrationId) {
        System.out.println("스텝2 요청 거절");
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

        // 유저 가져오기
        User user = registrationRepository.findById(registrationId).get().getUser();
        // 메세지 바꾸기
        setMailAndAlram(user,"스텝2 요청 거절","메일함을 확인해보세요");

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

        // 우체통 저장 및 푸시알람
        // 유저 가져오기
        User temp = registrationRepository.findById(registrationId).get().getUser();
        // 메세지 바꾸기
        setMailAndAlram(user,"입양완료 요청 수락","메일함을 확인해보세요");

        //예방 접종에 대해서 레절베이션 테이블에 추가

        MailboxReservation mailboxReservation = MailboxReservation.builder()
                .state("on")
                .type("medical")
                .title("예방 접종 정보")
                .detail("내 동물 정보에서 확인해주세요!")
                .time(LocalDate.now().plusWeeks(2))//2주 후
                .user(temp)
                .build();
        mailboxReservationRepository.save(mailboxReservation);


        System.out.println("koooooooooo"+getDateList().size());
        //사진에 대해서 레절베이션 테이블에 추가
        for(int i = 0 ; i<getDateList().size();i++){
            MailboxReservation mailboxReservation1 = MailboxReservation.builder()
                    .user(temp)
                    .time(getDateList().get(i))
                    .detail("1년동안 사진을 올려주셔야 합니다")
                    .title("사진을 올려주세요")
                    .type("photo")
                    .state("on")
                    .build();
            mailboxReservationRepository.save(mailboxReservation1);
            System.out.println(getDateList().get(i));
        }

        //todo 스케쥴러



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


        // 유저 가져오기
        User user = registrationRepository.findById(registrationId).get().getUser();
        // 메세지 바꾸기
        setMailAndAlram(user,"입양완료 요청 거절","메일함을 확인해보세요");


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
