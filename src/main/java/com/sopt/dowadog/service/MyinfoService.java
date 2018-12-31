package com.sopt.dowadog.service;

import com.sopt.dowadog.model.domain.*;
import com.sopt.dowadog.model.dto.MyinfoDto;
import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.repository.MailboxRepository;
import com.sopt.dowadog.repository.UserRepository;
import com.sopt.dowadog.util.ResponseMessage;

import com.sopt.dowadog.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyinfoService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    MailboxRepository mailboxRepository;


    //<마이페이지>
    //userID 정보 가져와서
    //좋아요 갯수-0
    //스크랩 갯수-0
    //내가 쓴글 갯수-0
    //사용자 정보

    //우체통 화면


    //todo <동물 정보 수정> userID 정보 가져와서 동물 정보 이름 종류 몸무게 나이

    //<사용자 정보 수정>
    //userID 정보 가져와서
    //이름
    //연락처
    //이메일

    //<좋아요 조회>
    //userID 정보 가져와서

    //<스크랩 조회>
    //userID 정보 가져와서

    //<내가 쓴글 조회>
    //userID 정보 가져와서

    //<우체통 조회>


    //userID 가져와서 animal 정보 가져오기  -> 이름 종류 몸무게 나이
    //userID 가져와서 좋아요, 스크랩, 내가쓴글 리스트 가져오기


    //todo 우체통 API작성 controller 작성하기 테이블도 구성되야함

    //UserID로 정보가져오기
    public DefaultRes<MyinfoDto> readMypageByUserId(final String userId) {
        User user = userRepository.findById(userId).get(); //user에 사용자 아이디 주고 정보 저장
//        좋아요 갯수
        int likeCount = user.getAnimalLikeCount();
//        스크랩 갯수
        int scrapCount = user.getCardnewsScrapCount();
//        내가쓴글 갯수
        int communityCount = user.getWrittenCommunityCount();
        boolean mailboxUpdateStatus = user.isNewMailbox();


        //유저가 가진 메일박스 리스트중에 안읽은 메일박스가 하나라도 존재하면 NEW 아이콘이 노출될지 결정하는 변수값을 바꿔준다
//        for(Mailbox mailbox : mailboxList) {
//            if(mailbox.isComplete() == false){
//                mailboxUpdateStatus = true;
//                break;
//            }
//        }

        //todo 입양한 동물에 대한 이름을 넣는것인지, 2마리이상이면 어떤거인지..

        MyinfoDto myinfoDto = MyinfoDto.builder()
                                 .userLike(likeCount)
                                 .userScrap(scrapCount)
                                 .userCommunity(communityCount)
                                 .mailboxUpdated(mailboxUpdateStatus)
                                 .build();

        //todo 사용자 정보 모두 보내기 , 우체통 보내기
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_MYINFO, myinfoDto);
    }

    //todo 마이페이지 로그아웃

    //사용자 정보 수정
    public DefaultRes<User> updateUserInfoByUserId(User modifiedUser, final String userId){
        User user = userRepository.getOne(userId);
        user.setName(modifiedUser.getName());
        user.setPhone(modifiedUser.getPhone());
        user.setEmail(modifiedUser.getEmail());
        user.setBirth(modifiedUser.getBirth());
        userRepository.save(user);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_MYINFO, user);
    }

    //todo 좋아요 등등의 page limit 갯수 정하기

    //사용자 좋아요 리스트 조회
    public DefaultRes<List<UserAnimalLike>> readLikeListByUserId(int page, int limit, String userId){
        Pageable pageable = PageRequest.of(page, limit);

        User user = userRepository.findById(userId).get();

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER_LIKE, user.getUserAnimalLikeList());
    }

    //사용자 스크랩 리스트 조회
    public DefaultRes<List<UserCardnewsScrap>> readClipsListByUserId(int page, int limit, String userId){
        Pageable pageable = PageRequest.of(page, limit);

        User user = userRepository.findById(userId).get();

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER_SCRAP, user.getUserCardnewsScrapList());
    }
    //사용자 작성한 글 리스트 조회
    public DefaultRes<List<Community>> readCommunityListByUserId(int page, int limit, String userId){
        Pageable pageable = PageRequest.of(page, limit);

        User user = userRepository.findById(userId).get();

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER_COMMUNITY, user.getCommunityList());
    }

    public DefaultRes<Mailbox> readMailboxesUserId(String userId){
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_MAILBOX, mailboxRepository.findByUserId(userId));
    }


}






