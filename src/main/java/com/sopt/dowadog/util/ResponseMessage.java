package com.sopt.dowadog.util;

public class ResponseMessage {

    //Login
    public static final String LOGIN_SUCCESS = "로그인 성공";
    public static final String LOGIN_FAIL = "로그인 실패";

    //Signup
    public static final String READ_USER = "회원 정보 조회 성공";
    public static final String NOT_FOUND_USER = "회원을 찾을 수 없습니다.";
    public static final String CREATED_USER = "회원 가입 성공";
    public static final String UPDATE_USER = "회원 정보 수정 성공";
    public static final String DELETE_USER = "회원 탈퇴 성공";



    //유기동물 관련 리스폰스 메세지
    public static final String READ_ANIMAL = "동물 조회 성공";
    public static final String NOT_FOUND_ANIMAL= "동물을 찾을 수 없다.";



    //Cardnews
    public static final String READ_CARDNEWS = "카드뉴스 조회 성공";
    public static final String READ_CARDNEWSCONTENTS = "카드뉴스 컨텐츠 조회 성공";

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


}
