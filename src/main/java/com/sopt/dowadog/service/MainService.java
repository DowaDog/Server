//package com.sopt.dowadog.service;
//
//import com.sopt.dowadog.model.common.DefaultRes;
//import com.sopt.dowadog.model.domain.Registration;
//import com.sopt.dowadog.model.domain.User;
//import com.sopt.dowadog.model.dto.MainDto;
//import com.sopt.dowadog.util.ResponseMessage;
//import com.sopt.dowadog.util.StatusCode;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class MainService {
//
//    @Autowired
//    UserService userService;
//
//    DefaultRes<MainDto> readMain(User user) {
//
//        // 게스트일경우 빈 객체 생성
//        MainDto mainDto = new MainDto();
//
//        if(user != null) {
//            mainDto = user.getMainDto();
//            lastRegistration = registration
//
//            List<Registration> registrationList = user.getRegistrationList();
//
//            for(Registration registration : registrationList) {
//                registration.getStatus();
//            }
//
////            registrationList.
//
//        }
//
//        //todo 임시 메시지
//        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ANIMAL, mainDto);
//
////         이부분 비즈니스로직으로 뺄것
//        if (this.registrationList.size() == 0) {
//            mainDto.setAdopting(false);
//        } else {
//
//            //현재 진행중인 마지막 registration 가져옴
//            Registration lastRegistration = this.registrationList.get(this.registrationList.size() - 1);
//
//
//            mainDto.setRegStatus(lastRegistration.getRegStatus());
//            mainDto.setUserCheck(lastRegistration.isUserCheck());
//
//
//            if (lastRegistration.getRegStatus().equals("deny")) mainDto.setAdopting(false);
//            lastRegistration.getRegStatus();
//
//
//            lastRegistration.getRegistrationMeeting().getMaterial()
//            private boolean login;
//
//            private boolean todayAdopt;
//
//            private Boolean registrationUpdated = null;
//            private Boolean mailboxUpdated = null;
//            private String status = null; // step1, step2, step3, step4, step5
//
//            //on step3
//            private String place = null;
//            private String time = null;
//            private String item = null;
//
//
//
//
//            if (communityRepository.findById(communityId).isPresent()) {
//                Community community = communityRepository.findById(communityId).get();
//                CommunityDto communityDto = community.getCommunityDto();
//                if (user != null) communityDto.setAuth(community.getAuth(user.getId()));
//
//                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_COMMUNITY, communityDto);
//            } else {
//                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_COMMUNITY);
//            }
//
//            return DefaultRes.FAIL_DEFAULT_RES;
//
//
//
//}
