package com.sopt.dowadog.scheduler;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopt.dowadog.model.domain.PublicAnimal;
import com.sopt.dowadog.repository.PublicAnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class PublicAnimalScheduler {

    @Value("${OPENAPI.KEY}")
    private String serviceKey;

    @Value("${OPENAPI.DOMAIN}")
    private String serviceDomain;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    PublicAnimalRepository publicAnimalRepository;


    @Scheduled(cron = "0 0 4 * * *") // 매일 새벽 4시에 동작
    public void readPublicAnimal() throws Throwable {


        //전체 삭제 이후
        publicAnimalRepository.deleteAll();


        final ObjectMapper mapper = new ObjectMapper();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYYMMdd"); //today yyyyMMdd

        //유기날짜 시작~종료
        String from = LocalDate.now().minusYears(2).format(formatter);
        String to = LocalDate.now().format(formatter);

        //요청 url - 데이터 기준 : 현재일자로부터 2년전까지의 유기 신고 대상의 공고중인 동물
        String requestUrl = UriComponentsBuilder
                .fromUriString(serviceDomain)
                .queryParam("bgnde", from)
                .queryParam("endde", to)
                .queryParam("pageNo", 1)
                .queryParam("state", "notice")
                .queryParam("numOfRows", 9999).toUriString();

        //key 파라미터
        String keyParam = new StringBuilder("&ServiceKey=").append(serviceKey).toString();
        //최종 요청 url
        String endPoint = new StringBuilder(requestUrl).append(keyParam).toString();

        URI uri = new URI(endPoint);

        //get Public data
        String obj = restTemplate.getForObject(uri, String.class);


        //json 파싱부분 todo 여기 리팩토링 필요
        Map map = mapper.readValue(obj, new TypeReference<Map<String, Map<String, Map>>>() {});

        Map<String ,Object> responseMap = mapper.convertValue(map.get("response"), Map.class);
        Map<String, Object> bodyMap = mapper.convertValue(responseMap.get("body"), Map.class);

        Map itemMap = mapper.convertValue(bodyMap.get("items"), Map.class);


        List<List<Object>> resultList = new ArrayList<List<Object>>(itemMap.values());

        List result = resultList.get(0);


        //insert row
        for(Object item : result){
            PublicAnimal publicAnimal = mapper.convertValue(item, PublicAnimal.class);
            publicAnimalRepository.save(publicAnimal);
        }


    }

}
