package com.sopt.dowadog.service.normal;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.Animal;
import com.sopt.dowadog.model.domain.Registration;
import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.model.dto.MainDto;
import com.sopt.dowadog.repository.AnimalRepository;
import com.sopt.dowadog.repository.MailboxRepository;
import com.sopt.dowadog.repository.RegistrationRepository;
import com.sopt.dowadog.util.ResponseMessage;
import com.sopt.dowadog.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class MainService {

    @Autowired
    UserService userService;
    @Autowired
    RegistrationRepository registrationRepository;
    @Autowired
    MailboxRepository mailboxRepository;
    @Autowired
    AnimalRepository animalRepository;




    public DefaultRes<MainDto> readMain(User user) {

        //입양 안했을 때(단계시작전) : NO
        //main 1단계 입양신청 : S0
        //main 2단계 전화 상담 : S1
        //main 3단계 직접 방문전 : S2
        //main 3단계 직접 방문후 : S3
        //main 4단계 : COMPLETE
        //단계별로 승인되지 않았을 때 : DENY


        System.out.println("### MAIN VIEW COME ###");

        // 게스트일경우 빈 객체 생성
        MainDto mainDto = new MainDto();

        //로그인 안했을시 뷰
        //login 안한 경우 view는 동일하게 고정 ( 클라쪽에선 맨처음 로그인 여부로 뷰 결정 )
        mainDto.setView("NO");

        if (user != null) {
            mainDto.setLogin(true);
            //
            //view 결정 비즈니스로직
            //
            //로그인 했을시 뷰 default
            mainDto.setView("NO");
            //
            Optional<Registration> lastRegistrationOptional = registrationRepository.findFirstByUserOrderByIdDesc(user);
            System.out.println("## 마지막 신청서 정보 ##");

            if (lastRegistrationOptional.isPresent()) {
                Registration lastRegistration = lastRegistrationOptional.get();
//                mainDto.setRegStatus(lastRegistration.getRegStatus());
                mainDto.setUserCheck(lastRegistration.isUserCheck());

                if (lastRegistration.getRegStatus().equals("complete")) { // 마지막 신청서가 입양 절차 완료일경우
                    System.out.println("## 마지막 신청서 : 입양 절차 완료 ##");

                    if (!lastRegistration.isUserCheck()) { // 유저체크 안함
                        System.out.println("## 마지막 신청서 : 유저가 확인하지 않았습니다 ##");
                        mainDto.setView("COMPLETE");
                    } else {
                        mainDto.setView("NO");
                    }
                } else {
                    System.out.println("## 마지막 신청서 : 입양 절차 완료되지 않았습니다 ##");

                    if (!lastRegistration.isValidReg()) {
                        System.out.println("##   마지막 신청서 : 유효한 신청서가 없습니다 ##");
                        if (!lastRegistration.isUserCheck()) {
                            mainDto.setView("DENY");
                        } else {
                            mainDto.setView("NO");
                        }
                    } else {
//                        mainDto.setAdopting(true);

                        switch (lastRegistration.getRegStatus()) {
                            case "step0":
                                mainDto.setView("S0");
                                break;
                            case "step1":
                                mainDto.setView("S1");
                                break;
                            case "step2":
                                mainDto.setView("S2");
                                mainDto.setPlace(lastRegistration.getMeetPlace());
                                mainDto.setTime(lastRegistration.getMeetTime());
                                mainDto.setMaterial(lastRegistration.getMeetMaterial());
                                break;
                            case "step3":
                                mainDto.setView("S3");
                                break;
                        }
                    }
                }
            }
        }
        //전체 동물의 수 출력
        mainDto.setTotalCount(animalRepository.findAnimalBy()+100);

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_MAIN, mainDto);
    }


    // 완료
    public DefaultRes checkMain(User user) {
        if (user != null) {
            System.out.println("## 메인뷰 : 회원 로그인 확인##");

            if (registrationRepository.findByUserAndUserCheck(user, false).isPresent()) { // 유저가 확인하지 않은 신청서에 대해서

                System.out.println("## 유저가 확인하지 않은 신청서가 존재합니다 ##");

                List<Registration> registrationList = registrationRepository.findByUserAndUserCheck(user, false).get();

                //true로 변경
                for (Registration registration : registrationList) {
                    System.out.println("## 유저 확인 완료 ##");
                    registration.setUserCheck(true);
                    registrationRepository.save(registration);
                }

            }
            ;
            return DefaultRes.res(StatusCode.OK, ResponseMessage.CHECK_MAIN);
        }

        System.out.println("#   # 메인뷰 : 메인뷰 : 비회원 확인##");

        return DefaultRes.res(StatusCode.OK, ResponseMessage.CHECK_MAIN);
    }

}