package com.sopt.dowadog.service;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.PublicAnimal;
import com.sopt.dowadog.model.dto.*;
import com.sopt.dowadog.repository.PublicAnimalRepository;
import com.sopt.dowadog.specification.PublicAnimalSpecification;
import com.sopt.dowadog.util.ResponseMessage;
import com.sopt.dowadog.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PublicAnimalService {

    @Autowired
    PublicAnimalRepository publicAnimalRepository;
    @Autowired
    AnimalService animalService;


    //타입이랑 종류 파씽 함수
    private List<String> getTypeAndKind(String kindCd){

        return Arrays.asList(kindCd.replaceAll("\\[","").split("\\]\\s"));

    }

    //스트링을 로컬데이트로 만드는 함수
    private LocalDate getDate(final String date){

        return LocalDate.parse(date, DateTimeFormatter.BASIC_ISO_DATE);
    }

    //지역에 대해서 처리 함수
    private String getRegion(final String region){
        List<String> temp = Arrays.asList(region.split("-"));
        String reg = temp.get(0);


        if(reg.equals("경북")||reg.equals("경남")||reg.equals("울산")||reg.equals("부산")||reg.equals("대구")){
            reg = "경상";
        }else if(reg.equals("세종")||reg.equals("충남")||reg.equals("충북")||reg.equals("대전")) {
            reg = "충청";

        }else if(reg.equals("광주")||reg.equals("전북")||reg.equals("전남")){
            reg = "전라";
        }else if(reg.equals("인천")){
            reg = "경기";
        }

        return  reg;


    }


    //todo 캐시적용 해야함 ( 스케줄링타이밍동안 문제 없도록 신경써야함 )
    public DefaultRes<PublicAnimalListDto> readPublicAnimalList(PublicAnimalSearchDto search, int page, int limit, String userId) {
        //todo 이부분에서 enum과 전혀 다른 걸 주면 에러난다...ㅎㅎ
        Map<String, Object> filter = new HashMap<>();
        Pageable pageable = PageRequest.of(page, limit);
        List<PublicListformDto> listform = new ArrayList<>();



        if (search.getType() != null) filter.put("type", search.getType());
        if (search.getRegion() != null) filter.put("region", search.getRegion());
        if (search.getRemainNoticeDate() != null) filter.put("remainNoticeDate", search.getRemainNoticeDate());

        Page<PublicAnimal> animals = publicAnimalRepository.findAll(PublicAnimalSpecification.searchPublicAnimal(filter), pageable);
        List<PublicAnimal> animalList = animals.getContent();



        for(PublicAnimal temp : animalList){
            PublicListformDto publicListformDto = temp.getListAnimalDto();
            publicListformDto.setNoticeEddt(getDate(temp.getNoticeEdt()));
            publicListformDto.setRegion(getRegion(temp.getNoticeNo()));
            publicListformDto.setType(getTypeAndKind(temp.getKindCd()).get(0));
            publicListformDto.setKindCd(getTypeAndKind(temp.getKindCd()).get(1));
            publicListformDto.setRemainDateState(animalService.getDdayState(getDate(temp.getNoticeEdt())));

            listform.add(publicListformDto);
        }





        PublicAnimalListDto animalListDto = PublicAnimalListDto.builder()
                .pageable(pageable)
                .content(listform)
                .build();

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ANIMAL,animalListDto);
    }


    //상세보기 구현

    //public DefaultRes<Animal>


}
