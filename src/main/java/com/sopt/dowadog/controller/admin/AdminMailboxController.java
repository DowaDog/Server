//package com.sopt.dowadog.controller.admin;
//
//import com.sopt.dowadog.model.domain.Mailbox;
//import com.sopt.dowadog.service.admin.AdminMailboxService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
///**
// * Created by kohen.kang on 2019-01-04..
// */
//
//@RequestMapping("api/admin/mailbox")
//@Controller
//public class AdminMailboxController {
//
//    @Autowired
//    AdminMailboxService adminMailboxService;
//
//    @PostMapping //전체 메일발송
//    public ResponseEntity createContentsMailbox(@ModelAttribute Mailbox mailbox) {
//
//        adminMailboxService.createMailboxes(mailbox);
//
//        return new ResponseEntity(HttpStatus.CREATED);
//    }
//}
