package com.sopt.dowadog.util;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.Future;

@Component
public class AsyncUtil {

    /*
    Bean의 경우 개발자가 컨트롤이 불가능한 외부 라이브러리들을 bean으로 등록하고 싶은 경우-> 디폴트로 오토와일 안 됨
    Component 경우 개발자가 컨트롤 가능한 Class들의 경우
     */
    // 여기 부분에서 서버키가 스트링으로 넣어주는데 컴포넌트를 넣어도 에러가 난다! 그냥 String하겠




    private final String serverKey = "AAAAnXucKic:APA91bGIaCKimGxoQxCl2nJBYYog90Xd18KR-R4t7uXSNo9XVz4vl25z8XqfkMd1jvEJUYHigkgDibhQFM_qbIGDcYnAkwXt0JxmY2FFDiLr4YOHXm4l0cHXrzGP3MIBCHRaprtIzkbd";
    //푸시 알람 메소드

    //단체 메세징
    @Async
    public Future<String> send(List<String> tokenArr, String title, String body) throws Exception{
        System.out.println(tokenArr.size());
        System.out.println(title);
        System.out.println(body);

        System.out.println("------서버키 테스트-------");
        System.out.println(serverKey);

        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization",new StringBuilder("key=").append(serverKey).toString());

        //HttpEntity 만들기 위한!
        JSONObject temp = new JSONObject();
        JSONObject notification = new JSONObject();

        temp.put("registration_ids",tokenArr);

        notification.put("title",title);
        notification.put("body",body);
        temp.put("notification", notification);
        //요구
        HttpEntity request = new HttpEntity(temp.toString(),headers);
        ResponseEntity<String> result = restTemplate.postForEntity("https://fcm.googleapis.com/fcm/send",request,String.class);

        System.out.println("푸시알람입니다.");
        System.out.println(result);
        return new AsyncResult<String>(result.getBody());

    }

    //단일 메세징
    @Async
    public Future<String> sendOne(String token, String title, String body) throws Exception{

        System.out.println(title);
        System.out.println(body);


        System.out.println("------서버키 테스트-------");
        System.out.println(serverKey);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.add("Authorization",new StringBuilder("key=").append(serverKey).toString());

        //HttpEntity 만들기 위한!
        JSONObject temp = new JSONObject();
        JSONObject notification = new JSONObject();

        temp.put("to",token);

        notification.put("title",title);
        notification.put("body",body);
        temp.put("notification", notification);
        //요구
        HttpEntity request = new HttpEntity(temp.toString(),headers);
        ResponseEntity<String> result = restTemplate.postForEntity("https://fcm.googleapis.com/fcm/send",request,String.class);

        System.out.println("푸시알람입니다.");

        System.out.println(result);

        return new AsyncResult<String>(result.getBody());

    }



}
