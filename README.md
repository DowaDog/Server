# Project
SOPT 23rd Appjam 기다릴개 서버 프로젝트




## Project spec 정보

Springboot Project

자바 버전 : JAVA 8
ORM 기술 : Hibernate JPA

..





## 서버 정보

|             | 역할 |도메인                                            |
| ----------- |- |------------------------------------------------- |
| **aws ec2** | API 애플리케이션 서버 |13.124.201.59                                     |
| **aws rds** | DB 서버 |rsh.cpcceaqwm3sy.ap-northeast-2.rds.amazonaws.com |





## 배포 단계 (1차 스펙)

#### 1. ec2 접속

```cmd
ssh -i "keyfile" ec2-user@13.124.201.59
```



#### 2. 파일전송

```cmd
scp -i "keyfile" "전송할 파일" ec2-user@13.124.201.59:~/
```



#### 3. jar 실행

```cmd
java -jar "파일명"
```





## API 설계



### 커뮤니티

| TYPE   | URI                                  | 설명                 |
| ------ | ------------------------------------ | -------------------- |
| GET    | /community?page={page}&limit={limit} | 커뮤니티 리스트 조회 |
| GET    | /community/{communityId}             | 커뮤니티 글 조회     |
| POST   | /community                           | 커뮤니티 글 생성     |
| PUT    | /community/{communityId}             | 커뮤니티 글 수정     |
| DELETE | /community/{communityId}             | 커뮤니티 글 삭제     |



### 댓글
| TYPE   | URI                               | 설명                                |
| ------ | --------------------------------- | ----------------------------------- |
| GET    | /community/{communityId}/comments | 특정 커뮤니티 글의 댓글 리스트 조회 |
| POST   | /community/{communityId}/comments | 특정 커뮤니티 글의 댓글 작성        |
| PUT    | /community/comments/{commentId}   | 댓글 수정                           |
| DELETE | /community/comments/{commentId}   | 댓글 삭제                           |



### 입양

| TYPE | URI                                                          | 설명                                                         |
| ---- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| GET  | /animals/emergency?page={page}&limit={limit}                 | 긴급 유기동물 목록                                           |
| GET  | /animals?searchParam={searchParam}&page={page}&limit={limit} | 최신순 유기 동물 목록<br />searchParam : type(String), startDate(Date), endDate(Date), region(String), tag(String), story(boolean) |
| GET  | /animals/{animalId}                                          | 유기동물 조회                                                |
| POST | /animals/{animalId}/registrations                            | 유기동물 신청서 작성                                         |
| POST | /animals/{animalId}/likes                                    | 유기동물 좋아요                                              |

### 마이페이지

| TYPE | URI                                             | 설명                  |
| ---- | ----------------------------------------------- | --------------------- |
| GET  | /mypage                                         | 마이페이지 조회       |
| PUT  | /mypage/animals                                 | 동물 정보 수정        |
| PUT  | /mypage/user                                    | 사람 정보 수정        |
| GET  | /mypage/likes/animals?page={page}&limit={limit} | 좋아요 리스트 조회    |
| GET  | /mypage/clips?page={page}&limit={limit}         | 스크랩 리스트 조회    |
| GET  | /mypage/community?page={page}&limit={limit}     | 내가 쓴글 리스트 조회 |
| GET  | /mypage/mailboxes                               | /mypage우체통 조회    |

GET 마이페이지 조회할때 : view에 있는 모든 정보를 다 넘김

- 사용자 정보

- 사용자 동물 정보

- 좋아요 갯수

- 스크랩 갯수

- 내가 쓴 글 갯수
