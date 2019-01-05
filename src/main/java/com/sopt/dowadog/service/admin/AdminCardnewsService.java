package com.sopt.dowadog.service.admin;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.Cardnews;
import com.sopt.dowadog.model.domain.CardnewsContents;
import com.sopt.dowadog.repository.CardnewsContentsRepository;
import com.sopt.dowadog.repository.CardnewsRepository;
import com.sopt.dowadog.service.FileService;
import com.sopt.dowadog.util.ResponseMessage;
import com.sopt.dowadog.util.S3Util;
import com.sopt.dowadog.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class AdminCardnewsService {

    @Autowired
    CardnewsRepository cardnewsRepository;

    @Autowired
    CardnewsContentsRepository cardnewsContentsRepository;

    @Autowired
    FileService fileService;

    @Value("${uploadpath.cardnews}")
    private String baseDir;

    @Value("${uploadpath.cardnewsContents}")
    private String baseDir1;

    @Value("${cloud.aws.endpoint}")
    private String s3Endpoint;

    //교육 카드뉴스 리스트 조회
    public DefaultRes<List<Cardnews>> readCardnewsEducationList(){
        //todo enum 객체 활용!
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CARDNEWS, cardnewsRepository.findByTypeOrderByCreatedAtDesc("education"));
    }

    //상식 카드뉴스 리스트 조회
    public DefaultRes<List<Cardnews>> readCardnewsKnowledgeList(int page, int limit){
        Pageable pageable = PageRequest.of(page, limit);
        //todo enum 객체 활용!
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CARDNEWS, cardnewsRepository.findByTypeOrderByCreatedAtDesc("knowledge", pageable));
    }

    //카드뉴스 컨텐츠 상세보기
    public DefaultRes<List<CardnewsContents>> readAllCardnewsContentsList(int cardnewsId){

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CARDNEWSCONTENTS, cardnewsContentsRepository.findByCardnewsId(cardnewsId));
    }

    //카드뉴스 생성
    public DefaultRes<Cardnews> createCardnewsService(Cardnews cardnews){

        MultipartFile cardnewsImgFile = cardnews.getCardnewsImgFile();

        if(cardnews.getCardnewsImgFile() != null){

            String filePath = new StringBuilder(baseDir).
                    append(S3Util.getUuid()).
                    append(cardnewsImgFile.getOriginalFilename()).toString();

           fileService.fileUpload(cardnewsImgFile, filePath);

           cardnews.setCardnewsImgFile(cardnewsImgFile);
           cardnews.setImgPath(filePath);
        }

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

        MultipartFile cardnewsContentsImgFile = cardnewsContents.getCardnewsContentsImgFile();

        if(cardnewsContents.getCardnewsContentsImgFile() != null){

            String filePath = new StringBuilder(baseDir1).
                    append(S3Util.getUuid()).
                    append(cardnewsContentsImgFile.getOriginalFilename()).toString();

            fileService.fileUpload(cardnewsContentsImgFile, filePath);

            cardnewsContents.setThumnailImg(filePath);
            cardnewsContents.setCardnewsContentsImgFile(cardnewsContentsImgFile);
        }

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
