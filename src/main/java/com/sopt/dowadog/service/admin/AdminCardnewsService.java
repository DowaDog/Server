package com.sopt.dowadog.service.admin;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.Cardnews;
import com.sopt.dowadog.model.domain.CardnewsContents;
import com.sopt.dowadog.model.domain.Mailbox;
import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.repository.CardnewsContentsRepository;
import com.sopt.dowadog.repository.CardnewsRepository;
import com.sopt.dowadog.repository.MailboxRepository;
import com.sopt.dowadog.repository.UserRepository;
import com.sopt.dowadog.service.FileService;
import com.sopt.dowadog.util.AsyncUtil;
import com.sopt.dowadog.util.ResponseMessage;
import com.sopt.dowadog.util.S3Util;
import com.sopt.dowadog.util.StatusCode;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class AdminCardnewsService {

    @Autowired
    CardnewsRepository cardnewsRepository;

    @Autowired
    CardnewsContentsRepository cardnewsContentsRepository;

    @Autowired
    FileService fileService;

    @Autowired
    MailboxRepository mailboxRepository;




    @Autowired
    UserRepository userRepository;




    @Value("${uploadpath.cardnews}")
    private String cardnewsBaseDir;

    @Value("${uploadpath.cardnewsContents}")
    private String cardnewsContentBaseDir;




    //교육 카드뉴스 리스트 조회
    public DefaultRes<List<Cardnews>> readCardnewsEducationList() {
        //todo enum 객체 활용!
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CARDNEWS, cardnewsRepository.findByTypeOrderByCreatedAtDesc("education"));
    }

    //상식 카드뉴스 리스트 조회
    public DefaultRes<List<Cardnews>> readCardnewsKnowledgeList(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        //todo enum 객체 활용!
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CARDNEWS, cardnewsRepository.findByTypeOrderByCreatedAtDesc("knowledge"));
    }

    //카드뉴스 컨텐츠 상세보기
    public DefaultRes<List<CardnewsContents>> readAllCardnewsContentsList(int cardnewsId) {

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CARDNEWSCONTENTS, cardnewsContentsRepository.findByCardnewsId(cardnewsId));
    }

    //카드뉴스 생성
    public DefaultRes<Cardnews> createCardnewsService(Cardnews cardnews) {
        try{
            List<User> userList = userRepository.findAll();
            System.out.println(userList.size());
            List<String> tokenArr = new ArrayList<>();
            AsyncUtil asyncUtil = new AsyncUtil();


            MultipartFile cardnewsImgFile = cardnews.getCardnewsImgFile();

            if (cardnews.getCardnewsImgFile() != null) {


                String filePath = S3Util.getFilePath(cardnewsBaseDir, cardnewsImgFile);
                fileService.fileUpload(cardnewsImgFile, filePath);
                cardnews.setImgPath(filePath);
            }

            cardnewsRepository.save(cardnews);

            for(User u : userList){
                //토큰 생성
                tokenArr.add(u.getDeviceToken());

                //메일박스 넣기

                Mailbox mailbox = Mailbox.builder()
                        .user(u)
                        .title(cardnews.getTitle())
                        .detail(cardnews.getSubtitle())
                        .complete(false)
                        .type("content")
                        .build();

                mailboxRepository.save(mailbox);
            }

            System.out.println(tokenArr.size());
            System.out.println();

            String a = asyncUtil.send(tokenArr,cardnews.getTitle(),cardnews.getSubtitle()).get();

            System.out.println(a);


            return DefaultRes.res(StatusCode.OK, ResponseMessage.CREATED_CARDNEWS);

        }catch (Exception e){
            e.printStackTrace();
            return DefaultRes.res(StatusCode.DB_ERROR,ResponseMessage.DB_ERROR);
        }
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
    public DefaultRes<Cardnews> deleteCardnewsById(int cardnewsId) {
        cardnewsRepository.deleteById(cardnewsId);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_CARDNEWS);
    }

    //카드뉴스 수정
    public DefaultRes<Cardnews> updateCardnewsById(Cardnews modifiedCardnews, int cardnewsId) {

        Cardnews cardnews = cardnewsRepository.getOne(cardnewsId);

        cardnews.setTitle(modifiedCardnews.getTitle());
        cardnews.setSubtitle(modifiedCardnews.getSubtitle());
        cardnews.setImgPath(modifiedCardnews.getImgPath());

        cardnewsRepository.save(cardnews);

        return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_CARDNEWS, cardnewsRepository.findById(cardnewsId).get());
    }

    //카드뉴스 컨텐츠 생성
    public DefaultRes createCardnewsContentsService(CardnewsContents cardnewsContents, int cardnewsId) {
        //todo 에러처리 해야댐!

        MultipartFile cardnewsContentsImgFile = cardnewsContents.getCardnewsContentsImgFile();

        if (cardnewsContents.getCardnewsContentsImgFile() != null) {

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
    public DefaultRes<CardnewsContents> deleteCardnewsContentsById(int cardnewsContentsId) {
        cardnewsContentsRepository.deleteById(cardnewsContentsId);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_CARDNEWSCONTENTS);
    }

    //카드뉴스 컨텐츠 수정
    public DefaultRes<CardnewsContents> updateCardnewsContentsById(CardnewsContents modifiedCardnewsContents, int cardnewsId) {

        CardnewsContents cardnewsContents = cardnewsContentsRepository.getOne(cardnewsId);
        cardnewsContents.setDetail(modifiedCardnewsContents.getDetail());
        cardnewsContents.setThumnailImg(modifiedCardnewsContents.getThumnailImg());

        return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_CARDNEWSCONTENTS, cardnewsContents);
    }

    public List<Cardnews> readAllCardnews(){
        return cardnewsRepository.findAll();

    }
}