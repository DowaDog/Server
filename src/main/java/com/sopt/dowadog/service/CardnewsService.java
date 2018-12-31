package com.sopt.dowadog.service;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.Cardnews;
import com.sopt.dowadog.repository.CardnewsRepository;
import com.sopt.dowadog.util.ResponseMessage;
import com.sopt.dowadog.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardnewsService {

    @Autowired
    CardnewsRepository cardnewsRepository;

    public DefaultRes<List<Cardnews>> readCardnewsEducationList(){
        //todo enum 객체 활용!
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CARDNEWS, cardnewsRepository.findByType("education"));
    }

    public DefaultRes<List<Cardnews>> readCardnewsKnowledgeList(int page, int limit){
        Pageable pageable = PageRequest.of(page, limit);
        //todo enum 객체 활용!
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CARDNEWS, cardnewsRepository.findByType("knowledge", pageable));
    }


}
