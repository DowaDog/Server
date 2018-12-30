package com.sopt.dowadog.controller.api;

import com.sopt.dowadog.model.domain.Community;
import com.sopt.dowadog.model.domain.CommunityComment;
import com.sopt.dowadog.service.CommunityCommentService;
import com.sopt.dowadog.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/community")
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
        return new ResponseEntity(communityService.readCommunityList(page, limit), HttpStatus.OK);
    }

    //커뮤니티 글 조회
    @GetMapping("{communityId}")
    public Object readCommunity(@PathVariable("communityId")int communityId) {
        return new ResponseEntity(communityService.readCommunityById(communityId), HttpStatus.OK);
    }


    //커뮤니티 글 생성
    @PostMapping
    public ResponseEntity createCommunity(Community community) throws Exception {
        return new ResponseEntity(communityService.createCommunityService(community), HttpStatus.CREATED);
    }


    //커뮤니티 글 수정
    @PutMapping("{communityId}")
    public ResponseEntity updateCommunity(Community community, @PathVariable("communityId") int communityId) {
        return new ResponseEntity(communityService.updateCommunityById(community, communityId), HttpStatus.OK);
    }


    //커뮤니티 글 삭제
    @DeleteMapping("{communityId}")
    public ResponseEntity deleteCommunity(@PathVariable("communityId") int communityId) {

        return new ResponseEntity(communityService.deleteCommunityById(communityId), HttpStatus.OK);
    }



    //특정 커뮤니티 글의 댓글 리스트 조회
    @GetMapping("{communityId}/comments")
    public ResponseEntity readCommentList(@PathVariable("communityId") int communityId) {

        return new ResponseEntity(communityCommentService.readCommunityCommentList(communityId), HttpStatus.CREATED);
    }

    //특정 커뮤니티 글의 댓글 작성
    @PostMapping("{communityId}/comments")
    public ResponseEntity createComment(@RequestBody CommunityComment communityComment, @PathVariable(name="communityId") int communityId) {

        return new ResponseEntity(communityCommentService.createCommunityComment(communityComment, communityId), HttpStatus.OK);
    }

    //댓글 수정
    @PutMapping("comments/{commnetId}")
    public ResponseEntity updateComment(@RequestBody CommunityComment communityComment, @PathVariable("commnetId") int commentId) {

        return new ResponseEntity(communityCommentService.updateCommunityComment(communityComment, commentId), HttpStatus.OK);
    }

    //댓글 삭제
    @DeleteMapping("comments/{commentId}")
    public ResponseEntity deleteComment(@PathVariable("commentId") int commentId) {
        return new ResponseEntity(communityCommentService.deleteCommunityComment(commentId), HttpStatus.OK);
    }



}