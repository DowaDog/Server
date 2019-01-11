package com.sopt.dowadog.service.normal;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.Cardnews;
import com.sopt.dowadog.model.domain.CardnewsContents;
import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.model.dto.AllEducatedDto;
import com.sopt.dowadog.model.dto.CardnewsContentsDto;
import com.sopt.dowadog.model.dto.CardnewsContentsListDto;
import com.sopt.dowadog.repository.CardnewsContentsRepository;
import com.sopt.dowadog.repository.CardnewsRepository;
import com.sopt.dowadog.util.ResponseMessage;
import com.sopt.dowadog.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CardnewsContentsService {

    @Autowired
    CardnewsRepository cardnewsRepository;

    @Autowired
    CardnewsContentsRepository cardnewsContentsRepository;

    @Value("${cloud.aws.endpoint}")
    private String s3Endpoint;

    public DefaultRes<CardnewsContentsListDto> readAllCardnewsContentsList(User user, int cardnewsId){
        if(user != null) {
            List<CardnewsContents> cardnewsContentsList = cardnewsContentsRepository.findByCardnewsId(cardnewsId);

            Cardnews cardnews = cardnewsRepository.findById(cardnewsId).get();

            List<CardnewsContentsDto> cardnewsContentsDtoList = new ArrayList<>();

            for (CardnewsContents cardnewsContents : cardnewsContentsList) {
                CardnewsContentsDto cardnewsContentsDto = cardnewsContents.getCardnewsContentsDto();

                String temp = s3Endpoint + cardnewsContents.getThumnailImg();

                cardnewsContentsDto.setThumnailImg(temp);

                cardnewsContentsDto = setCardnewsContentsDtoAuth(user, cardnewsContentsDto);

                cardnewsContentsDtoList.add(cardnewsContentsDto);
            }

            AllEducatedDto allEducatedDto = getAllEducatedDtoComplete(user);

            CardnewsContentsListDto cardnewsContentsListDto = CardnewsContentsListDto.builder().
                    cardnewsThumbnail(new StringBuilder(s3Endpoint).append(cardnews.getImgPath()).toString()).
                    content(cardnewsContentsDtoList).
                    edu(allEducatedDto).
                    type(cardnews.getType()).
                    build();

            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CARDNEWSCONTENTS, cardnewsContentsListDto);
        }else{


            List<CardnewsContents> cardnewsContentsList = cardnewsContentsRepository.findByCardnewsId(cardnewsId);
            Cardnews cardnews = cardnewsRepository.findById(cardnewsId).get();

            List<CardnewsContentsDto> cardnewsContentsDtoList = new ArrayList<>();

            for (CardnewsContents cardnewsContents : cardnewsContentsList) {
                CardnewsContentsDto cardnewsContentsDto = cardnewsContents.getCardnewsContentsDto();

                String temp = s3Endpoint + cardnewsContents.getThumnailImg();

                cardnewsContentsDto.setThumnailImg(temp);

                cardnewsContentsDto = setCardnewsContentsDtoAuth(user, cardnewsContentsDto);

                cardnewsContentsDtoList.add(cardnewsContentsDto);
            }


            CardnewsContentsListDto cardnewsContentsListDto = CardnewsContentsListDto.builder().
                    cardnewsThumbnail(new StringBuilder(s3Endpoint).append(cardnews.getImgPath()).toString()).
                    content(cardnewsContentsDtoList).
                    edu(new AllEducatedDto()).
                    type(cardnews.getType()).
                    build();
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CARDNEWSCONTENTS, cardnewsContentsListDto);
        }
    }

    public CardnewsContentsDto setCardnewsContentsDtoAuth(User user ,CardnewsContentsDto cardnewsContentsDto){
        if (user != null) cardnewsContentsDto.setAuth(user.getAuth(user.getId()));

        return cardnewsContentsDto;
    }

    public AllEducatedDto getAllEducatedDtoComplete(User user){
        int allEducate = 0;
        int userEducate = 0;
        boolean allComplete = false;

        if(cardnewsRepository.findByType("education").isPresent()){//전체 교육 개수
            allEducate = cardnewsRepository.findByType("education").get().size();
            userEducate = user.getCardnewsEducatedCount(); //사용자가교육한 갯수
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
}
