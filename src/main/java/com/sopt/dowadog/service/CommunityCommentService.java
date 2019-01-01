package com.sopt.dowadog.service;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.Community;
import com.sopt.dowadog.model.domain.CommunityComment;
import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.model.dto.CommentDto;
import com.sopt.dowadog.model.dto.CommunityDto;
import com.sopt.dowadog.repository.CommunityCommentRepository;
import com.sopt.dowadog.repository.CommunityRepository;
import com.sopt.dowadog.util.ResponseMessage;
import com.sopt.dowadog.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.xml.stream.events.Comment;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommunityCommentService {

    //todo 생성자 주입 기법으로 변경
    @Autowired
    CommunityCommentRepository communityCommentRepository;

    @Autowired
    CommunityRepository communityRepository;

    //todo 여기 검증 제대로-----

    //완
    public DefaultRes<CommunityComment> createCommunityComment(User user, CommunityComment communityComment, int communityId){
//         todo 여기 예외처리들 null이라던가

        if(communityRepository.findById(communityId).isPresent()) {
            communityComment.setCommunity(communityRepository.findById(communityId).get());
            communityComment.setUser(user);
        } else {
            return DefaultRes.NOT_FOUND;
        }

        return DefaultRes.res(StatusCode.OK, ResponseMessage.CREATED_COMMENT, communityCommentRepository.save(communityComment));
    }

    //완료
    public DefaultRes<List<CommentDto>> readCommunityCommentList(User user, int communityId){

        List<CommunityComment> communityCommentList = communityCommentRepository.findByCommunityId(communityId);
        List<CommentDto> commentDtoList = new ArrayList<>();

        for(CommunityComment communityComment : communityCommentList) {
            CommentDto commentDto = communityComment.getCommentDto();
            if(user != null) commentDto.setAuth(communityComment.getAuth(user.getId()));

            commentDtoList.add(commentDto);
        }
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_COMMENT, commentDtoList);
    }

    public DefaultRes<CommunityComment> updateCommunityComment(User user, CommunityComment modifiedCommunityComment, int commentId){
        if(!communityCommentRepository.findById(commentId).isPresent()){
            return DefaultRes.NOT_FOUND;
        }

        CommunityComment communityComment = communityCommentRepository.findById(commentId).get();

        if(communityComment.getAuth(user.getId())) {
            communityComment.setDetail(modifiedCommunityComment.getDetail());
            return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_COMMENT, communityCommentRepository.save(communityComment));
        }
        return DefaultRes.UNAUTHORIZATION;
    }

    public DefaultRes deleteCommunityComment(User user, int commentId){

        if(!communityCommentRepository.findById(commentId).isPresent()){
            return DefaultRes.NOT_FOUND;
        }
        CommunityComment communityComment = communityCommentRepository.findById(commentId).get();

        if(communityComment.getAuth(user.getId())) {
            communityCommentRepository.deleteById(commentId);
            return DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_COMMENT);
        }

        return DefaultRes.UNAUTHORIZATION;


    }
}
