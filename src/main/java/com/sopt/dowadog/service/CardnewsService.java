package com.sopt.dowadog.service;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.Cardnews;
import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.model.domain.UserCardnewsEducate;
import com.sopt.dowadog.model.domain.UserCardnewsScrap;
import com.sopt.dowadog.model.dto.AllEducatedDto;
import com.sopt.dowadog.model.dto.CardnewsDto;
import com.sopt.dowadog.model.dto.CardnewsListDto;
import com.sopt.dowadog.repository.CardnewsRepository;
import com.sopt.dowadog.repository.UserCardnewsEducateRepository;
import com.sopt.dowadog.repository.UserCardnewsScrapRepository;
import com.sopt.dowadog.repository.UserRepository;
import com.sopt.dowadog.util.ResponseMessage;
import com.sopt.dowadog.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CardnewsService {

    @Autowired
    CardnewsRepository cardnewsRepository;

    @Autowired
    UserCardnewsEducateRepository userCardnewsEducateRepository;


    @Autowired
    UserCardnewsScrapRepository userCardnewsScrapRepository;

    @Autowired
    UserRepository userRepository;

    @Value("${cloud.aws.endpoint}")
    private String s3Endpoint;

    public DefaultRes<CardnewsListDto> readCardnewsEducationList(User user){
        //todo edu 정보 여기서 주는거 아니고 컨텐츠 상세 조회에서 보여주는것 Dto에 Auth 정보 추가
        if(userRepository.findById(user.getId()).isPresent()){

            List<Cardnews> cardnewsList = cardnewsRepository.findByTypeOrderByCreatedAtDesc("education");

            List<CardnewsDto> cardnewsDtoList =  new ArrayList<>();


            for(Cardnews cardnews : cardnewsList){
                CardnewsDto cardnewsDto = cardnews.getCardnewsDto();

                String temp = s3Endpoint + cardnews.getImgPath();

                cardnewsDto.setImgPath(temp);

                cardnewsDto = setCardnewsDtoAuth(user, cardnewsDto);

                cardnewsDto.setEducated(cardnews.getEducated(user));

                cardnewsDtoList.add(cardnewsDto);
            }

            AllEducatedDto allEducatedDto = getAllEducatedDtoComplete(user);

            CardnewsListDto cardnewsListDto = CardnewsListDto.builder().
                    content(cardnewsDtoList).
                    build();

            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CARDNEWS, cardnewsListDto);
        }else{
            List<Cardnews> cardnewsList = cardnewsRepository.findByTypeOrderByCreatedAtDesc("education");

            List<CardnewsDto> cardnewsDtoList =  new ArrayList<>();

            for(Cardnews cardnews : cardnewsList){
                CardnewsDto cardnewsDto = cardnews.getCardnewsDto();

                String temp = s3Endpoint + cardnews.getImgPath();

                cardnewsDto.setImgPath(temp);

                cardnewsDtoList.add(cardnewsDto);
            }

            CardnewsListDto cardnewsListDto = CardnewsListDto.builder()
                    .content(cardnewsDtoList)
                    .build();

            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CARDNEWS, cardnewsListDto);
        }
    }

    public DefaultRes<CardnewsListDto> readCardnewsKnowledgeList(int page, int limit){
        Pageable pageable = PageRequest.of(page, limit);

        Page<Cardnews> cardnewsPage = cardnewsRepository.findByTypeOrderByCreatedAtDesc("knoewledge", pageable);
        Pageable paging = cardnewsPage.getPageable();

        List<Cardnews> cardnewsList = cardnewsPage.getContent();

        List<CardnewsDto> cardnewsDtoList = new ArrayList<>();

        for(Cardnews cardnews : cardnewsList){
            CardnewsDto cardnewsDto = cardnews.getCardnewsDto();

            cardnewsDtoList.add(cardnewsDto);
        }

        CardnewsListDto cardnewsListDto = CardnewsListDto.builder()
                .content(cardnewsDtoList)
                .build();
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CARDNEWS, cardnewsListDto);
    }

    public DefaultRes<List<UserCardnewsEducate>> createCardnewsEducated(User user, final int cardnewsId) {

        List<UserCardnewsEducate> checkEduList = userCardnewsEducateRepository.findByUser_IdAndCardnews_Id(user.getId(), cardnewsId);

        if (checkEduList.size() == 0) {
            Optional<User> eduUser = userRepository.findById(user.getId());
            Optional<Cardnews> eduCardnews = cardnewsRepository.findById(cardnewsId);

            if (!eduUser.isPresent()) {
                return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.NOT_FOUND);
            }
            UserCardnewsEducate userCardnewsEducate = new UserCardnewsEducate();
            userCardnewsEducate.setCardnews(eduCardnews.get());
            userCardnewsEducate.setUser(eduUser.get());

            userCardnewsEducateRepository.save(userCardnewsEducate);
        } else {
            return DefaultRes.res(StatusCode.OK, ResponseMessage.ALREADY_EXIST);
        }
        return DefaultRes.res(StatusCode.CREATED, ResponseMessage.COMPLETE_CARDNEWS, userCardnewsEducateRepository.findByUser_IdAndCardnews_Id(user.getId(), cardnewsId));
    }

    @Transactional
    public DefaultRes<UserCardnewsScrap> createCardnewsScrap(User user, final int cardnewsId){
        List<UserCardnewsScrap> checkScrapList = userCardnewsScrapRepository.findByUser_IdAndCardnews_Id(user.getId(),cardnewsId);

        if(checkScrapList.size() == 0) {
            Optional<User> scrapUser = userRepository.findById(user.getId());
            Optional<Cardnews> scrapCardnews = cardnewsRepository.findById(cardnewsId);

            if(!scrapCardnews.isPresent()) {
                return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.NOT_FOUND);
            }

            UserCardnewsScrap userCardnewsScrap = new UserCardnewsScrap();
            userCardnewsScrap.setCardnews(scrapCardnews.get());
            userCardnewsScrap.setUser(scrapUser.get());

            userCardnewsScrapRepository.save(userCardnewsScrap);
        } else {

            userCardnewsScrapRepository.deleteByUser_IdAndCardnews_Id(user.getId(), cardnewsId);
            return DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_SCRAP);
        }
        return DefaultRes.res(StatusCode.CREATED, ResponseMessage.SCRAP_CARDNEWS);
    }

    public AllEducatedDto getAllEducatedDtoComplete(User user){
        int allEducate = 0;
        int userEducate = 0;
        boolean allComplete = false;
        if(cardnewsRepository.findByType("education").isPresent()){//전체 교육 개수
            allEducate = cardnewsRepository.findByType("education").get().size();

            if(!Optional.ofNullable(user).isPresent()){

                userEducate = 0;
            }else{

                userEducate = user.getCardnewsEducatedCount() ; //사용자가교육한 갯수
            }

            if(allEducate == userEducate){
                allComplete = true;
            }
            AllEducatedDto allEducatedDto = new AllEducatedDto();
            allEducatedDto.setAllEducate(allEducate);
            allEducatedDto.setUserEducated(userEducate);
            allEducatedDto.setAllComplete(allComplete);
            return allEducatedDto;
        }else{
            return null;
        }
    }

    public CardnewsDto setCardnewsDtoAuth(User user ,CardnewsDto cardnewsDto){
        if (user != null) cardnewsDto.setAuth(user.getAuth(user.getId()));

        return cardnewsDto;
    }
}
