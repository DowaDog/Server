//package com.sopt.dowadog.service.admin;
//
//import com.sopt.dowadog.enumeration.MailboxTypeEnum;
//import com.sopt.dowadog.model.domain.Mailbox;
//import com.sopt.dowadog.model.domain.User;
//import com.sopt.dowadog.repository.MailboxRepository;
//import com.sopt.dowadog.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class AdminMailboxService {
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    MailboxRepository mailboxRepository;
//
//    public void createMailboxes(Mailbox mailbox) {
//
//
//        System.out.println(mailbox.toString());
//        String title = mailbox.getTitle();
//        String detail = mailbox.getDetail();
//
//
//
//        List<User> userList = userRepository.findAll();
//
//        for(User user : userList){
//            Mailbox newMailbox = Mailbox.builder()
//                                    .user(user)
//                                    .title(title)
//                                    .detail(detail)
//                                    .type(MailboxTypeEnum.REGISTRATION.getKey())
//                                    .imgPath(MailboxTypeEnum.REGISTRATION.getValue())
//                                    .build();
//
//            mailboxRepository.save(newMailbox);
//        }
//
//
//    }
//}
