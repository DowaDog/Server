package com.sopt.dowadog.util;

public class ResponseMessage {

    //Login
    public static final String LOGIN_SUCCESS = "로그인 성공";
    public static final String LOGIN_FAIL = "로그인 실패";
    public static final String AUTH_FAIL = "인증 실패";
    public static final String AUTH_SUCCESS = "인증 성공";

    //Signup
    public static final String READ_USER = "회원 정보 조회 성공";
    public static final String NOT_FOUND_USER = "회원을 찾을 수 없습니다.";
    public static final String CREATED_USER = "회원 가입 성공";
    public static final String UPDATE_USER = "회원 정보 수정 성공";
    public static final String DELETE_USER = "회원 탈퇴 성공";


    // 좋아요 기능
    public static final String CREATED_LIKE = "좋아요 추가 성공";
    public static final String DELETED_LIKE = "좋아요 취소 성공";


    //유기동물 관련 리스폰스 메세지
    public static final String READ_ANIMAL = "동물 조회 성공";
    public static final String NOT_FOUND_ANIMAL= "동물을 찾을 수 없다.";



    //Cardnews
    public static final String READ_CARDNEWS = "카드뉴스 조회 성공";
    public static final String CREATED_CARDNEWS = "카드뉴스 생성 성공";
    public static final String DELETE_CARDNEWS = "카드뉴스 삭제 성공";
    public static final String UPDATE_CARDNEWS = "카드뉴스 수정 성공";
    public static final String NOT_FOUND_CARDNNEWS = "카드뉴스 정보를 찾을 수 없습니다.";

    //카드뉴스 contents
    public static final String READ_CARDNEWSCONTENTS = "카드뉴스 컨텐츠 조회 성공";
    public static final String CREATED_CARDNEWSCONTENTS = "카드뉴스 컨텐츠 생성 성공";
    public static final String DELETE_CARDNEWSCONTENTS = "카드뉴스 컨텐츠 삭제 성공";
    public static final String UPDATE_CARDNEWSCONTENTS = "카드뉴스 컨텐츠 수정 성공";
    public static final String NOT_FOUND_CARDNEWSCONTENTS = "카드뉴스 컨텐츠 정보를 찾을 수 없습니다.";

    //Community
    public static final String READ_COMMUNITY = "커뮤니티 정보 조회 성공";
    public static final String CREATED_COMMUNITY = "커뮤니티 정보 생성 성공";
    public static final String UPDATE_COMMUNITY = "커뮤니티 정보 수정 성공";
    public static final String DELETE_COMMUNITY = "커뮤니티 정보 삭제 성공";
    public static final String NOT_FOUND_COMMUNITY = "커뮤니티 정보를 찾을 수 없습니다.";


    //Comment
    public static final String READ_COMMENT = "댓글 정보 조회 성공";
    public static final String CREATED_COMMENT = "댓글 정보 생성 성공";
    public static final String UPDATE_COMMENT = "댓글 정보 수정 성공";
    public static final String DELETE_COMMENT = "댓글 정보 삭제 성공";
    public static final String NOT_FOUND_COMMENT = "댓글 정보를 찾을 수 없습니다.";


    //Common

    public static final String INTERNAL_SERVER_ERROR = "서버 내부 에러";

    public static final String DB_ERROR = "데이터베이스 에러";


    //MYPAGE

    public static final String READ_MYINFO = "마이페이지 정보 조회 성공";
    public static final String UPDATE_USER_ANIMAL = "사용자 동물 정보 수정 성공";
    public static final String UPDATE_MYINFO = "사용자 정보 수정 성공";
    public static final String READ_USER_LIKE = "사용자 좋아요 조회 성공";
    public static final String READ_USER_SCRAP = "사용자 스크랩 조회 성공";
    public static final String READ_USER_COMMUNITY = "사용자 쓴글 조회 성공";


    //MAILBOX

    public static final String READ_MAILBOX = "우체통 정보 조회 성공";


}
