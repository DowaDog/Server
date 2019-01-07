package com.sopt.dowadog.controller.api.normal;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.Community;
import com.sopt.dowadog.model.domain.CommunityComment;
import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.service.normal.CommunityCommentService;
import com.sopt.dowadog.service.normal.CommunityService;
import com.sopt.dowadog.service.common.JwtService;
import com.sopt.dowadog.service.normal.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

        System.out.println("#######     api/normal/community   GET #######");

        try {
            User user = null; //게스트의 경우 널값으로 유저 생성해서 넘겨줌

            if (userService.getUserByJwtToken(jwtToken) != null) {
                user = userService.getUserByJwtToken(jwtToken);
            }
            return new ResponseEntity(communityService.readCommunityList(user, page, limit), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //커뮤니티 글 조회
    @GetMapping("{communityId}")
    public ResponseEntity readCommunity
    (@RequestHeader(value = "Authorization", required = false) final String jwtToken,
     @PathVariable("communityId") int communityId) {

        System.out.println("#######     api/normal/community/:communityId   GET #######");

        try {
            User user = null;
            if (userService.getUserByJwtToken(jwtToken) != null) {
                user = userService.getUserByJwtToken(jwtToken);
            }
            return new ResponseEntity(communityService.readCommunityById(user, communityId), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //커뮤니티 글 생성
    @PostMapping
    public ResponseEntity createCommunity
    (@RequestHeader(value = "Authorization", required = false) final String jwtToken,
     Community community) throws Exception {

        System.out.println("#######     api/normal/community   POST #######");

        try {
            User user = userService.getUserByJwtToken(jwtToken);
            return new ResponseEntity(communityService.createCommunity(user, community), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //커뮤니티 글 수정
    @PutMapping("{communityId}")
    public ResponseEntity updateCommunity
    (@RequestHeader(value = "Authorization", required = false) final String jwtToken,
     Community community, @PathVariable("communityId") int communityId) {

        System.out.println("#######     api/normal/community/:communityId   PUT #######");

        try {
            User user = userService.getUserByJwtToken(jwtToken);
            return new ResponseEntity(communityService.updateCommunityById(user, community, communityId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //커뮤니티 수정시 사진 삭제
    @DeleteMapping("{communityId}/communityimg/{communityImgId}")
    public ResponseEntity deleteCommunityImg(@RequestHeader(value = "Authorization") final String jwtToken,
                                             @PathVariable("communityImgId") int communityImgId){

        System.out.println("#######     api/normal/community/:communityId/communityimg/:communityImgId   DELETE #######");


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
    (@RequestHeader(value = "Authorization", required = false) final String jwtToken,
     @PathVariable("communityId") int communityId) {

        System.out.println("#######     api/normal/community/{communityId}   DELETE #######");

        try {
            User user = userService.getUserByJwtToken(jwtToken);
            return new ResponseEntity(communityService.deleteCommunityById(user, communityId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //todo here
    //특정 커뮤니티 글의 댓글 리스트 조회
    @GetMapping("{communityId}/comments")
    public ResponseEntity readCommentList
    (@RequestHeader(value = "Authorization", required = false) final String jwtToken,
     @PathVariable("communityId") int communityId) {

        System.out.println("#######     api/normal/community/{communityId}/comments   GET #######");

        try {
            User user = userService.getUserByJwtToken(jwtToken);
            return new ResponseEntity(communityCommentService.readCommunityCommentList(user, communityId), HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //특정 커뮤니티 글의 댓글 작성
    @PostMapping("{communityId}/comments")
    public ResponseEntity createComment
    (@RequestHeader(value = "Authorization", required = false) final String jwtToken,
     @RequestBody CommunityComment communityComment, @PathVariable(name = "communityId") int communityId) {

        System.out.println("#######     api/normal/community/{communityId}/comments   POST #######");

        try {
            User user = userService.getUserByJwtToken(jwtToken);
            return new ResponseEntity(communityCommentService.createCommunityComment(user, communityComment, communityId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //댓글 수정
    @PutMapping("comments/{commnetId}")
    public ResponseEntity updateComment
    (@RequestHeader(value = "Authorization", required = false) final String jwtToken,
     @RequestBody CommunityComment communityComment, @PathVariable("commnetId") int commentId) {

        System.out.println("#######     api/normal/community/{communityId}/comments   PUT #######");

        try {
            User user = userService.getUserByJwtToken(jwtToken);
            return new ResponseEntity(communityCommentService.updateCommunityComment(user, communityComment, commentId), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    //댓글 삭제
    @DeleteMapping("comments/{commentId}")
    public ResponseEntity deleteComment
    (@RequestHeader(value = "Authorization", required = false) final String jwtToken,
     @PathVariable("commentId") int commentId) {

        System.out.println("#######     api/normal/community/{communityId}/comments   DELETE #######");

        try {
            User user = userService.getUserByJwtToken(jwtToken);
            return new ResponseEntity(communityCommentService.deleteCommunityComment(user, commentId), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}