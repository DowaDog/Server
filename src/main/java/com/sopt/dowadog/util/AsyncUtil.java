package com.sopt.dowadog.util;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.Future;

public class AsyncUtil {
   //@Value이 왜 안 되는지? 궁금

    private String serverKey = "AAAAqVW_Pns:APA91bHYq_DWihTDjhWAmScR-RIEJj2UND8N2UGZ-Klu3uSI04MU3oLlp1O7IGTXF_h-ld5Bq1xLmbZHSNYByIw7OZjxsFKQTjGtbQ_uc88dvX30H0kp3EpQl-EeTKgn2PF9nSjRX0q0";

    //푸시 알람 메소드

    //단체 메세징
    @Async
    public Future<String> send(List<String> tokenArr, String title, String body) throws Exception{
        System.out.println(tokenArr.size());
        System.out.println(title);
        System.out.println(body);

        RestTemplate restTemplate = new RestTemplate();

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

        return new AsyncResult<String>(result.getBody());

    }

    //단일 메세징
    @Async
    public Future<String> sendOne(String token, String title, String body) throws Exception{

        System.out.println(title);
        System.out.println(body);

        RestTemplate restTemplate = new RestTemplate();

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

        return new AsyncResult<String>(result.getBody());

    }



}
