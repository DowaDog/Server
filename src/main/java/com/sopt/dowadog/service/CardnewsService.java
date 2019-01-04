package com.sopt.dowadog.service;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.Cardnews;
import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.model.domain.UserCardnewsEducate;
import com.sopt.dowadog.model.domain.UserCardnewsScrap;
import com.sopt.dowadog.model.dto.UserCardnewsEducateDto;
import com.sopt.dowadog.repository.CardnewsRepository;
import com.sopt.dowadog.repository.UserCardnewsEducateRepository;
import com.sopt.dowadog.repository.UserCardnewsScrapRepository;
import com.sopt.dowadog.repository.UserRepository;
import com.sopt.dowadog.util.ResponseMessage;
import com.sopt.dowadog.util.StatusCode;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    public DefaultRes<List<Cardnews>> readCardnewsEducationList(){
        //todo enum 객체 활용!
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CARDNEWS, cardnewsRepository.findByTypeOrderByCreatedAtDesc("education"));
    }

    public DefaultRes<List<Cardnews>> readCardnewsKnowledgeList(int page, int limit){
        Pageable pageable = PageRequest.of(page, limit);
        //todo enum 객체 활용!
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CARDNEWS, cardnewsRepository.findByTypeOrderByCreatedAtDesc("knowledge", pageable));
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

    public DefaultRes<UserCardnewsScrap> createCardnewsScrap(User user, final int cardnewsId){
        List<UserCardnewsScrap> checkScrapList = userCardnewsScrapRepository.findByUser_IdAndCardnews_Id(user.getId(),cardnewsId);

        if(checkScrapList.size() == 0) {
            Optional<User> scrapUser = userRepository.findById(user.getId());
            Optional<Cardnews> scrapCardnews = cardnewsRepository.findById(cardnewsId);

            if(!scrapUser.isPresent()) {
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
}
