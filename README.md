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
| GET  | /mypage/scrap?page={page}&limit={limit}         | 스크랩 리스트 조회    |
| GET  | /mypage/community?page={page}&limit={limit}     | 내가 쓴글 리스트 조회 |
| GET  | /mypage/mailboxes                               | /mypage우체통 조회    |

GET 마이페이지 조회할때 : view에 있는 모든 정보를 다 넘김

- 사용자 정보

- 사용자 동물 정보

- 좋아요 갯수

- 스크랩 갯수

- 내가 쓴 글 갯수


## Log 설정

### logback 사용

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <include resource="org/springframework/boot/logging/logback/base.xml"/>

    <appender name="dailyRollingFileAppender" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <prudent>true</prudent>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>./log/dowadog.%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>DEBUG</level>
        </filter>

        <encoder>
            <pattern>%d{yyyy:MM:dd HH:mm:ss.SSS} %-5level --- [%thread] %logger{35} : %msg %n</pattern>
        </encoder>
    </appender>

    <logger name="org.springframework.web" level="INFO"/>
    <logger name="com.sopt.dowadog" level="INFO"/>
    <logger name="org.hibernate.sql" level="ERROR"/>

    <root level="INFO">
        <appender-ref ref="dailyRollingFileAppender" />
    </root>
</configuration>
```

* maxHistory : 30일
* Logging 수준 : Debug 이상만 세팅(전역), 각 패키지별 Logging 수준 별도 세팅





# Ehcache

```xml
<?xml version="1.0" encoding="UTF-8"?>
<ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:noNamespaceSchemaLocation="ehcache.xsd"
         updateCheck="true" monitoring="autodetect"
         dynamicConfig="true">

    <defaultCache
            maxElementsInMemory="10000"
            eternal="false"
            timeToIdleSeconds="120"
            timeToLiveSeconds="120"
            memoryStoreEvictionPolicy="LRU"
            overflowToDisk="false"/>

    <cache name="snapshotData"
           maxElementsInMemory="10000"
           eternal="false"
           maxEntriesLocalHeap="10000"
           maxEntriesLocalDisk="10000"
           diskSpoolBufferSizeMB="20"
           timeToLiveSeconds="600"
           memoryStoreEvictionPolicy="LRU"
           overflowToDisk="false"
           transactionalMode="off">
    </cache>
</ehcache>
```

* defaultCache : 기본 캐시설정 (@Cacheable만 사용시) 
* snapshotData : 공공데이터 조회의 경우 스케줄러를 이용한 스냅샷 데이터이므로 매번 조회하는데 연산을 쓰지 않아도 됨. 이러한 부분에 리소스를 줄이기 위해 ehCache 적용
* 공공데이터 갱신에는 10000건 이하정도의 데이터 객체가 delete/insert 되므로 메모리에저장하는 객체 수 제한을 10000으로 정하였고, 5분의 갱신주기를 가지게끔 하였음. ( delete/insert 사이 과정에 의한 데이터 불일치 가능성때문에 큰 폭으로 설정하지 않음 )

