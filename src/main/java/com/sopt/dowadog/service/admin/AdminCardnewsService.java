package com.sopt.dowadog.service.admin;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.Cardnews;
import com.sopt.dowadog.model.domain.CardnewsContents;
import com.sopt.dowadog.repository.CardnewsContentsRepository;
import com.sopt.dowadog.repository.CardnewsRepository;
import com.sopt.dowadog.util.ResponseMessage;
import com.sopt.dowadog.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AdminCardnewsService {

    @Autowired
    CardnewsRepository cardnewsRepository;

    @Autowired
    CardnewsContentsRepository cardnewsContentsRepository;

    //카드뉴스 생성
    public DefaultRes<Cardnews> createCardnewsService(Cardnews cardnews){
        return DefaultRes.res(StatusCode.OK, ResponseMessage.CREATED_CARDNEWS, cardnewsRepository.save(cardnews));
    }

    //카드뉴스 삭제
    public DefaultRes<Cardnews> deleteCardnewsById(int cardnewsId){
        cardnewsRepository.deleteById(cardnewsId);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_CARDNEWS);
    }

    //카드뉴스 수정
    public DefaultRes<Cardnews> updateCardnewsById(Cardnews modifiedCardnews, int cardnewsId){

        Cardnews cardnews = cardnewsRepository.getOne(cardnewsId);

        cardnews.setTitle(modifiedCardnews.getTitle());
        cardnews.setSubtitle(modifiedCardnews.getSubtitle());
        cardnews.setImgPath(modifiedCardnews.getImgPath());

        cardnewsRepository.save(cardnews);

        return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_CARDNEWS, cardnewsRepository.findById(cardnewsId).get());
    }

    //카드뉴스 컨텐츠 생성
    public DefaultRes<CardnewsContents> createCardnewsContentsService(CardnewsContents cardnewsContents, int cardnewsId){
        //todo 에러처리 해야댐!
        cardnewsContents.setCardnews(cardnewsRepository.findById(cardnewsId).get());
        return DefaultRes.res(StatusCode.OK, ResponseMessage.CREATED_CARDNEWSCONTENTS, cardnewsContentsRepository.save(cardnewsContents));
    }

    //카드뉴스 컨텐츠 삭제
    public DefaultRes<CardnewsContents> deleteCardnewsContentsById(int cardnewsContentsId){
        cardnewsContentsRepository.deleteById(cardnewsContentsId);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_CARDNEWSCONTENTS);
    }

    //카드뉴스 컨텐츠 수정
    public DefaultRes<CardnewsContents> updateCardnewsContentsById(CardnewsContents modifiedCardnewsContents, int cardnewsId){

        CardnewsContents cardnewsContents = cardnewsContentsRepository.getOne(cardnewsId);
        cardnewsContents.setDetail(modifiedCardnewsContents.getDetail());
        cardnewsContents.setThumnailImg(modifiedCardnewsContents.getThumnailImg());

        return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_CARDNEWSCONTENTS, cardnewsContents);
    }
}
