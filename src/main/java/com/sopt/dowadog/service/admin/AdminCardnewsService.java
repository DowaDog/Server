package com.sopt.dowadog.service.admin;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.Cardnews;
import com.sopt.dowadog.model.domain.CardnewsContents;
import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.model.dto.*;
import com.sopt.dowadog.repository.CardnewsContentsRepository;
import com.sopt.dowadog.repository.CardnewsRepository;
import com.sopt.dowadog.service.FileService;
import com.sopt.dowadog.util.ResponseMessage;
import com.sopt.dowadog.util.S3Util;
import com.sopt.dowadog.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
    private String cardnewsBaseDir;

    @Value("${uploadpath.cardnewsContents}")
    private String cardnewsContentBaseDir;

    @Value("${cloud.aws.endpoint}")
    private String s3Endpoint;

    //교육 카드뉴스 리스트 조회
    public DefaultRes<CardnewsListDto> readCardnewsEducationList(){
        //todo edu 정보 여기서 주는거 아니고 컨텐츠 상세 조회에서 보여주는것 Dto에 Auth 정보 추가
            List<Cardnews> cardnewsList = cardnewsRepository.findByTypeOrderByCreatedAtDesc("education");

            List<CardnewsDto> cardnewsDtoList =  new ArrayList<>();


            for(Cardnews cardnews : cardnewsList){
                CardnewsDto cardnewsDto = cardnews.getCardnewsDto();

                String temp = s3Endpoint + cardnews.getImgPath();

                cardnewsDto.setImgPath(temp);

                cardnewsDtoList.add(cardnewsDto);
            }

            CardnewsListDto cardnewsListDto = CardnewsListDto.builder().
                    content(cardnewsDtoList).
                    build();

            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CARDNEWS, cardnewsListDto);
    }

    //상식 카드뉴스 리스트 조회
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

    //카드뉴스 컨텐츠 상세보기
    public DefaultRes<CardnewsContentsListDto> readAllCardnewsContentsList(int cardnewsId){
        List<CardnewsContents> cardnewsContentsList = cardnewsContentsRepository.findByCardnewsId(cardnewsId);

        List<CardnewsContentsDto> cardnewsContentsDtoList = new ArrayList<>();

        for(CardnewsContents cardnewsContents : cardnewsContentsList){
            CardnewsContentsDto cardnewsContentsDto = cardnewsContents.getCardnewsContentsDto();

            cardnewsContentsDtoList.add(cardnewsContentsDto);
        }
        CardnewsContentsListDto cardnewsContentsListDto = CardnewsContentsListDto.builder().content(cardnewsContentsDtoList).build();

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CARDNEWSCONTENTS, cardnewsContentsListDto);
    }

    //카드뉴스 생성
    public DefaultRes<Cardnews> createCardnewsService(Cardnews cardnews){

        MultipartFile cardnewsImgFile = cardnews.getCardnewsImgFile();

        if(cardnews.getCardnewsImgFile() != null){

            String filePath = S3Util.getFilePath(cardnewsBaseDir, cardnewsImgFile);
//                    new StringBuilder(cardnewsBaseDir).
//                    append(S3Util.getUuid()).
//                    append(cardnewsImgFile.getOriginalFilename()).toString();

           fileService.fileUpload(cardnewsImgFile, filePath);

           cardnews.setImgPath(filePath);
        }

        cardnewsRepository.save(cardnews);

        return DefaultRes.res(StatusCode.OK, ResponseMessage.CREATED_CARDNEWS);
    }

//    String filePath = S3Util.getFilePath(baseDir, imgFile);
//
//                fileService.fileUpload(imgFile, filePath); // s3 upload
//
//    CommunityImg communityImg = CommunityImg.builder()
//            .community(community)
//            .filePath(filePath)
//            .originFileName(imgFile.getOriginalFilename())
//            .build();

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
    public DefaultRes createCardnewsContentsService(CardnewsContents cardnewsContents, int cardnewsId){
        //todo 에러처리 해야댐!

        MultipartFile cardnewsContentsImgFile = cardnewsContents.getCardnewsContentsImgFile();

        if(cardnewsContents.getCardnewsContentsImgFile() != null){

            String filePath = S3Util.getFilePath(cardnewsContentBaseDir, cardnewsContentsImgFile);
//                    new StringBuilder(cardnewsContentBaseDir).
//                    append(S3Util.getUuid()).
//                    append(cardnewsContentsI  mgFile.getOriginalFilename()).toString();


            fileService.fileUpload(cardnewsContentsImgFile, filePath);

            cardnewsContents.setThumnailImg(filePath);
            cardnewsContents.setCardnewsContentsImgFile(cardnewsContentsImgFile);
        }

        cardnewsContents.setCardnews(cardnewsRepository.findById(cardnewsId).get());
        cardnewsContentsRepository.save(cardnewsContents);

        return DefaultRes.res(StatusCode.OK, ResponseMessage.CREATED_CARDNEWSCONTENTS);
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




//    admin용

    public CardnewsListDto readCardnews() {

        List<Cardnews> cardnewsList = cardnewsRepository.findAll();

        List<CardnewsDto> cardnewsDtoList = new ArrayList<>();

        for(Cardnews cardnews : cardnewsList){
            CardnewsDto cardnewsDto = cardnews.getCardnewsDto();

            cardnewsDtoList.add(cardnewsDto);
        }
        CardnewsListDto cardnewsListDto = CardnewsListDto.
                builder().content(cardnewsDtoList).
                build();

        return cardnewsListDto;
    }
}
