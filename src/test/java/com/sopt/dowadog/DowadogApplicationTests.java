package com.sopt.dowadog;

import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.repository.UserRepository;
import com.sopt.dowadog.service.care.CareRegistrationService;
import com.sopt.dowadog.util.AES256Util;
import com.sopt.dowadog.util.AsyncUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DowadogApplicationTests {

    @Autowired
    CareRegistrationService careRegistrationService;
    @Autowired
    UserRepository userRepository;

    @Test
    public void contextLoads() {




        try{
            AES256Util aes256Util = new AES256Util("12345678901234567890123456789012");
            System.out.println(aes256Util.aesEncode("010-5505-4102"));
            System.out.println("010-5505-4102");

//            User gahi = userRepository.findById("sksksksk").get();
//            careRegistrationService.setMailAndAlram(gahi, "가", "희");
//
//            AsyncUtil asyncUtil = new AsyncUtil();
//            System.out.println("#################### MESSAGE GO");
//            asyncUtil.sendOne("dPbvt1dPaWc:APA91bENJ7bCUykj0czZYYDNd0nTmIzMUJgbLBBtVqYIyn9yfXHAMN43oAkthxglXejb8PySaz8OwxKuHmw3tmqdr5n0teyfFRuWgsEyw0p2UN9IxzwguW0auNWdjS8EeYZ7vSy5qGpi", "푸시테스트입니다 :D", "안녕하세요 기다릴개입니다 ^^");
//            System.out.println("#################### MESSAGE END");

        } catch (Exception e){
            System.out.println(e);
        }
    }

}

