package com.sopt.dowadog.service;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.Community;
import com.sopt.dowadog.model.domain.CommunityComment;
import com.sopt.dowadog.repository.CommunityCommentRepository;
import com.sopt.dowadog.repository.CommunityRepository;
import com.sopt.dowadog.util.ResponseMessage;
import com.sopt.dowadog.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunityCommentService {

    //todo 생성자 주입 기법으로 변경
    @Autowired
    CommunityCommentRepository communityCommentRepository;

    @Autowired
    CommunityRepository communityRepository;

    public DefaultRes<CommunityComment> createCommunityComment(CommunityComment communityComment,  int communityId){
        // todo 여기 예외처리들 null이라던가
        communityComment.setCommunity(communityRepository.findById(communityId).get());

        return DefaultRes.res(StatusCode.OK, ResponseMessage.CREATED_COMMENT, communityCommentRepository.save(communityComment));
    }

    public DefaultRes<List<CommunityComment>> readCommunityCommentList(int communityId){

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_COMMENT, communityCommentRepository.findByCommunityIdOrderByCreatedAtDesc(communityId));
    }

    public DefaultRes<CommunityComment> updateCommunityComment(CommunityComment modifiedCommunityComment, int communityId){

        CommunityComment communityComment = communityCommentRepository.getOne(communityId);
        communityComment.setDetail(modifiedCommunityComment.getDetail());

        return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_COMMENT, communityComment);
    }

    public DefaultRes<CommunityComment> deleteCommunityComment(int communityId){

        communityCommentRepository.deleteById(communityId);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_COMMENT);
    }
}
