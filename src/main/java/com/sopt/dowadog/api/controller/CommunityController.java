package com.sopt.dowadog.api.controller;


import com.sopt.dowadog.model.DefaultRes;
import com.sopt.dowadog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api/community")
@Controller
public class CommunityController {

    //read, create, update, delete
    @Autowired
    UserService userService;

    //커뮤니티 리스트 조회
    @GetMapping
    public ResponseEntity readCommunityList
            (@RequestParam(name="page", defaultValue="0",required=false)int page,
             @RequestParam(name="limit", defaultValue="10", required=false)int limit) {

        return new ResponseEntity(userService.readUserList(), HttpStatus.NOT_FOUND);
    }

    @GetMapping("/t")
    public ResponseEntity readCommunityListT
            (@RequestParam(name="page", defaultValue="0",required=false)int page,
             @RequestParam(name="limit", defaultValue="10", required=false)int limit) {

        return new ResponseEntity(userService.readUserListOrdinary(), HttpStatus.OK);
    }

    //커뮤니티 글 조회
    @GetMapping("/{communityId}")
    public Object readCommunity(@PathVariable("communityId")int communityId) {
        return new Object();
    }


    //커뮤니티 글 생성
    @PostMapping
    public List<Object> communityList
    (@RequestParam(name="page", defaultValue="0",required=false)int page,
     @RequestParam(name="limit", defaultValue="10", required=false)int limit) {

        return new ArrayList<>();
    }


}
