package com.sopt.dowadog.service.admin;

import com.sopt.dowadog.enumeration.MailboxTypeEnum;
import com.sopt.dowadog.model.domain.Mailbox;
import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.repository.MailboxRepository;
import com.sopt.dowadog.repository.UserRepository;
import com.sopt.dowadog.util.AsyncUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminMailboxService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MailboxRepository mailboxRepository;

    @Autowired
    AsyncUtil asyncUtil;

    public void createMailboxes(Mailbox mailbox) {


        System.out.println(mailbox.toString());
        String title = mailbox.getTitle();
        String detail = mailbox.getDetail();
        String type = mailbox.getType();


        List<User> userList = userRepository.findAll();

        for (User user : userList) {
            Mailbox newMailbox = Mailbox.builder()
                    .user(user)
                    .title(title)
                    .detail(detail)
                    .type(type)
                    .build();

            mailboxRepository.save(newMailbox);
        }


        try {
            List<String> tokenArr = new ArrayList<>();

            List<User> notnulluserList = userRepository.findAllByNotDeviceToken();
            for (User u : notnulluserList) {
                //토큰 생성
                tokenArr.add(u.getDeviceToken());
            }

            asyncUtil.send(tokenArr, title, detail);
        } catch (Exception e) {
            System.out.println("Mailbox create Push");
            e.printStackTrace();
        }


    }
}
