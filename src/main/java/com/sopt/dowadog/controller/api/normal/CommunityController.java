package com.sopt.dowadog.controller.api.normal;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.Community;
import com.sopt.dowadog.model.domain.CommunityComment;
import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.service.CommunityCommentService;
import com.sopt.dowadog.service.CommunityService;
import com.sopt.dowadog.service.JwtService;
import com.sopt.dowadog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("api/normal/community")
@Controller
public class CommunityController {

    //read, create, update, delete
    @Autowired
    CommunityService communityService;

    @Autowired
    CommunityCommentService communityCommentService;

    @Autowired
    JwtService jwtService;

    @Autowired
    UserService userService;

    //커뮤니티 리스트 조회
    @GetMapping
    public ResponseEntity readCommunityList
    (@RequestHeader(value = "Authorization", required = false) final String jwtToken,
     @RequestParam(name = "page", defaultValue = "0") int page,
     @RequestParam(name = "limit", defaultValue = "10") int limit) {

        try {
            User user = userService.getUserByJwtToken(jwtToken);

            return new ResponseEntity(communityService.readCommunityList(user, page, limit), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //커뮤니티 글 조회
    @GetMapping("{communityId}")
    public Object readCommunity
    (@RequestHeader(value = "Authorization", required = false) final String jwtToken,
     @PathVariable("communityId") int communityId) {
        return new ResponseEntity(communityService.readCommunityById(communityId), HttpStatus.OK);
    }


    //커뮤니티 글 생성
    @PostMapping
    public ResponseEntity createCommunity
    (@RequestHeader(value = "Authorization") final String jwtToken,
     Community community) throws Exception {
        try {
            User user = userService.getUserByJwtToken(jwtToken);
            return new ResponseEntity(communityService.createCommunityService(user, community), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //커뮤니티 글 수정
    @PutMapping("{communityId}")
    public ResponseEntity updateCommunity
    (@RequestHeader(value = "Authorization") final String jwtToken,
     Community community, @PathVariable("communityId") int communityId) {
        return new ResponseEntity(communityService.updateCommunityById(community, communityId), HttpStatus.OK);
    }

    //커뮤니티 수정시 사진 삭제
    @DeleteMapping("{communityId}/communityimg/{communityImgId}")
    public ResponseEntity deleteCommunityImg(@RequestHeader(value = "Authorization") final String jwtToken,
                                             @PathVariable("communityImgId") int communityImgId){

        return new ResponseEntity(communityService.deleteCommunityImgById(communityImgId), HttpStatus.OK);
    }

//    @PostMapping("{communityId}/communityimg/")
//    public ResponseEntity createCommunityImg(@RequestHeader(value = "Authorization") final String jwtToken,
//                                             @PathVariable("communityId") int communityId) {
//
//        return new ResponseEntity(communityService.create)
//    }

    //커뮤니티 글 삭제
    @DeleteMapping("{communityId}")
    public ResponseEntity deleteCommunity
    (@RequestHeader(value = "Authorization") final String jwtToken,
     @PathVariable("communityId") int communityId) {

        return new ResponseEntity(communityService.deleteCommunityById(communityId), HttpStatus.OK);
    }


    //특정 커뮤니티 글의 댓글 리스트 조회
    @GetMapping("/{communityId}/comments")
    public ResponseEntity readCommentList
    (@RequestHeader(value = "Authorization", required = false) final String jwtToken,
     @PathVariable("communityId") int communityId, HttpServletRequest req) {

        //req.getAttribute("name");

        return new ResponseEntity(communityCommentService.readCommunityCommentList(communityId), HttpStatus.CREATED);
    }

    //특정 커뮤니티 글의 댓글 작성
    @PostMapping("{communityId}/comments")
    public ResponseEntity createComment
    (@RequestHeader(value = "Authorization") final String jwtToken,
     @RequestBody CommunityComment communityComment, @PathVariable(name = "communityId") int communityId) {

        return new ResponseEntity(communityCommentService.createCommunityComment(communityComment, communityId), HttpStatus.OK);
    }

    //댓글 수정
    @PutMapping("comments/{commnetId}")
    public ResponseEntity updateComment
    (@RequestHeader(value = "Authorization") final String jwtToken,
     @RequestBody CommunityComment communityComment, @PathVariable("commnetId") int commentId) {

        return new ResponseEntity(communityCommentService.updateCommunityComment(communityComment, commentId), HttpStatus.OK);
    }

    //댓글 삭제
    @DeleteMapping("comments/{commentId}")
    public ResponseEntity deleteComment
    (@RequestHeader(value = "Authorization") final String jwtToken,
     @PathVariable("commentId") int commentId) {
        return new ResponseEntity(communityCommentService.deleteCommunityComment(commentId), HttpStatus.OK);
    }

}