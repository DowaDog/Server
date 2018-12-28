package com.sopt.dowadog.api;

import com.sopt.dowadog.model.domain.Community;
import com.sopt.dowadog.model.domain.CommunityComment;
import com.sopt.dowadog.service.CommunityCommentService;
import com.sopt.dowadog.service.CommunityService;
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
    CommunityService communityService;

    @Autowired
    CommunityCommentService communityCommentService;

    //커뮤니티 리스트 조회
    @GetMapping
    public ResponseEntity readCommunityList
    (@RequestParam(name="page", defaultValue="0",required=false)int page,
     @RequestParam(name="limit", defaultValue="10", required=false)int limit) {

        return new ResponseEntity(communityService.readAllCommunityList(), HttpStatus.OK);
    }

    //커뮤니티 글 조회
    @GetMapping("/{communityId}")
    public Object readCommunity(@PathVariable("communityId")int communityId) {
        return new Object();
    }


    //커뮤니티 글 생성
    @PostMapping
    public ResponseEntity communityList(Community community) {

        return new ResponseEntity(communityService.createCommunityService(community), HttpStatus.OK);
    }


    //커뮤니티 글의 댓글 생성
    @PostMapping("/{communityId}/comments")
    public ResponseEntity createComment(@PathVariable(name="communityId") int communityId, @RequestBody CommunityComment communityComment) {

        return new ResponseEntity(communityCommentService.createCommunityComment(communityId, communityComment), HttpStatus.OK);
    }


}