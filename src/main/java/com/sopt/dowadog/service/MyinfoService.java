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

    //todo 우체통 API작성 controller 작성하기 테이블도 구성되야함

    //UserID로 정보가져오기
    public DefaultRes<MyinfoDto> readMypageByUserId(final String userId) {

        User user = userRepository.findById(userId).get(); //user에 사용자 아이디 주고 정보 저장
        String userName = user.getName();

        //입양된 동물의 마지막 리스트 이름
        String animalName = user.getUserAdoptAnimalName();

//      좋아요 갯수
        int likeCount = user.getAnimalLikeCount();
//        스크랩 갯수
        int scrapCount = user.getCardnewsScrapCount();
//        내가쓴글 갯수
        int communityCount = user.getWrittenCommunityCount();

        boolean mailboxUpdateStatus = user.isNewMailbox(); //메일박스 새로운게 있는지 확인


        //유저가 가진 메일박스 리스트중에 안읽은 메일박스가 하나라도 존재하면 NEW 아이콘이 노출될지 결정하는 변수값을 바꿔준다
//        for(Mailbox mailbox : mailboxList) {
//            if(mailbox.isComplete() == false){
//                mailboxUpdateStatus = true;
//                break;
//            }
//        }

        MyinfoDto myinfoDto = MyinfoDto.builder()
                                 .userName(userName)
                                 .userLike(likeCount)
                                 .userScrap(scrapCount)
                                 .userCommunity(communityCount)
                                 .mailboxUpdated(mailboxUpdateStatus)
                                 .animalName(animalName)
                                 .build();

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_MYINFO, myinfoDto);
    }

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
        User user = userRepository.findById(userId).get();
        Pageable pageable = PageRequest.of(page, limit);

        //int start = new PageRequest(page, limit).getOffset(); //검색을 7원하는 페이지,  페이지당 건수 , 정렬 방법
        //int end = (start + new pageable(page , limit).getPageSize()) > user.getAnimalLikeCount() ? user.getAnimalLikeCount() : (start + user.getAnimalLikeCount());
        // 시작 페이지 와 페이지의 크기를 더한것, 이 좋아요 리스트 개수보다 많으면
        //user.subList(start, end), pageable, user.getAnimalLikeCount();


        //List<UserAnimalLike> page = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        //return page;

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






