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
## 패키지 설명



| 패키지 명                    | 간략한 설명                                                  |
| ---------------------------- | ------------------------------------------------------------ |
| com.sopt.dowadog.annotation  | 사용자 인증을 위한 어노테이션                                |
| com.sopt.dowadog.aop         | - 사용자 인증을 위한 어노테이션의 포인트컷 적용 시점(CRUD 중 R을 뺀 나머지에 적용하기 위해 사용) <br/>-보호소 웹 인증에 대한 포인트 컷 적용<br/>-푸시 알람및 신청서에 대한 시스템 사용 히스토리(추후 구현 예정) |
| com.sopt.dowadog.config      | -jpa 설정<br/>- restTemplate 사용을 위해 설정<br/>- 캐시 사용 위한 설정 |
| com.sopt.dowadog.controller  | 컨트롤러 관련 클래스                                         |
| com.sopt.dowadog.enumeration | 우편함 종류와 토큰에 대한 expired 기간 지정                  |
| com.sopt.dowadog.model       | dto, 도메인 관련 클래스                                      |
| com.sopt.dowadog.repository  | Repository 관련한 클래스                                     |
| com.sopt.dowadog.scheduler   | 푸시알람과 공공데이터 업뎃을 위한 스케듈러 관련 클래스       |
| com.sopt.dowadog.service     | 서비스 관련 클래스                                           |
| com.sopt.dowadog.specifation | 필터와 검색을 위한 criteria 적용 클래스                      |
| com.sopt.dowadog.util        | -fcm 사용을 위한 async 적용한 클래스<br/>-단방향/양방향 암호화를 위한 클래스<br/>-S3 주소를 위한 클래스<br/>-responsemessage와 statuscode를 위한 클래스 |


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




#ec2에서 실시간 로그 보기
~~~
tail -f nohup.out
~~~



# 어노테이션 추가적 사용 설명



##@Scheduled




##@Asyn

#### - 스프링내에서 별도의 쓰레드에서 실행되면서 비동기로 실행하도록 한다.
#### - 이 프로젝트 내에서는 fcm을 사용하기 위해서 사용되었다.




### 조건

##### 1. public 메소드에서 적용

##### 2. 같은 클래스 안에서 async 메소드를 호출하는 셀프 호출의 경우 작동하지 않는다.

##### 3. 기본적으로 리턴 타입이 없는 메소드의 경우 void 로 처리하지만 리턴타입이 있는 경우 Future 객체에 실체 리턴값 넣어서 리턴타입이 있는 메소드로 적용함

##### 4. Application 클래스에 @EnableAsync를 추가해준다.




## @OneToMany 와 @ManyToOne의 어노테이션 사용으로 인한 무한참조

#### 해결방법

~~~
도메인 클래스 상단에 다음과 같은 어노테이션 붙이기
1) @JsonIdentityInfo(generator= ObjectIdGenerators.IntSequenceGenerator.class, property="@id")

~~~

 이 결과는 @id 프로퍼티를 갖은 1씩 증가된 아이디가 생성된다. 그 이유는 Jackson이 자동으로 증가는 아이디를 형성하였기 때문이다. 이 결과 값은 클라이언트에 쓸모 없는 값임으로 이거 없이 데이터를 가져오는 방법은 밑 해결방법을 이용했더니 해결되었다.

~~~
도메인 클래스 상단에 다음과 같은 어노테이션 붙이기
2) @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope=Animal.class
~~~

다음과 같은 방법은 Jackson이 기존의 id 값을 속성으로 잡고 독립적인 scope를 정해서 데이터를 가져올 수 있도록 하였다.

---

## ec2에 spring 내장 톰캣을 이용해서 jar파일로 실행되는 프로젝트에 letsencrypt로 https 설정하기

###문제 발생
>문제 1 : Self-Certificated된 인증서의 문제
문제 2 : 스프링의 내장 톰캣에 적용하는 방법

###해결방안
> 문제 1에 대한 해답 : 최초에 시도했던 방법으로, local에 직접 trust-group을 만들고 keytool을 이용해서 나 스스로 인증된 certification파일을 만들어서 이를 통해 스스로 인증된 https 통신을 시도하는 방식이다. 이 방식의 경우 spring의 내장 톰캣에 직접 붙일 수 있었고, application.properties 파일에 ssl관련 설정을 넣어주면 작동하는 방식이었기 때문에 굉장히 편리했지만 외부에서 접근하지 못했다.
그래서 우선 Self-Certification 문제를 해결하기 위해 무료 ssl인증서를 발급해주는 letsencrypt를 이용해서 인증서를 발급받아서 처리하는 것을 시도했다. 공인 인증된 trust-group을 통해 발급받은 인증서이기 때문에 외부에서도 접근할 수 있었다.

> 문제 2에 대한 해답 : letsencrypt를 통해 발급받은 인증서를 spring의 내장톰캣에 붙여보려는 시도를 해봤으나 성공하지 못해서 찾아낸 방법이 앞에 외장proxy서버를 두고 외장 proxy서버 뒤에 우리의 WAS서버를 위치하게 해서 요청을 redirect받는 방식을 이용했다.
nginx 서버에 ssl 설정을 해두고 443포트를 열어두었다. 그리고 80포트로 오는 요청을 443포트로 전환해서 오게만들고, 443포트로 받은 요청을 뒤에 위치해잇는 WAS서버의 8080포트로 redirect하는 방식으로 https를 우리의 WAS서버에 적용하는 것에 성공했다.

---

## Nginx를 이용한 road-balancing

### 현재 상태와 기능 추가 과정
> 우리는 https를 위해 Nginx를 외장 서버로 사용하고 proxy 서버로 동작하고 있는 Nginx를 통해서 ssl인증을 받은 뒤 우리의 어플리케이션 서버로 요청을 리다이렉트하는 방식으로 사용하고 있었다.
Nginx는 road-balancing을 간단한 방법으로 제공해주는데 upstream이라는 옵션을 통해서 설정할 수 있다. 디폴트로 round-robin방식을 사용하여 로드 밸런싱해주는 것으로 보이는데
이 upstream 옵션안에 분산시켜서 요청할 서버의 도메인을 각각 넣어주고 각각 마다 설정을 넣어줄 수 있다. weight로 가중치를 주면 자주 선택되는 서버를 선택할 수 있고, least_conn은 연결이 가장 적은 서버로 트래픽을 전달하는 역할을 한다.
현재 우리는 우리가 미리 구성해둔 같은 기능의 어플리케이션 서버를 각각 다른 도메인으로 두개 가지고 있으니, upstream 옵션에 두 도메인을 각각 server로 잡아주고, server 블록 안에 proxy_pass에 upstream 옵션 이름을 지정해주는 것으로 
로드 밸런싱 기능을 완료했다. 어떠한 서버가 메인이 되어야 한다거나 하는 것이 정해지지 않았기 때문에 기본 방식인 round-robin으로 놔두어 균등하게 분산되게 하였고, weight도 두지 않은 상태로 만약 패킷이 두개 들어온다면 각 서버에 하나씩 로드 밸런싱되어 들어가게 된다.

### 로드밸런싱을 했을 때 얻을 수 있는 장점
1. 이렇게 서비스를 할 때 로드 밸런서를 두고 서버를 두개 놓고 서비스를 하게 되면 얻을 수 있는 가장 큰 이득은 중단 없는 재배포가 가능하다는 점이다.
   2대의 서버중 1대가 작동을 멈추어도 서비스를 온전히 계속 제공할 수 있으므로 재배포나 유지 보수부분에서 얻을 수 있는 것이 많다.
2. 저가의 서버를 여러대 이용해서 고가의 서버를 사용하는 것처럼 사용할 수 있다는 것도 장점이 될 수 있을 것이다.
3. 또한 혹여 추후 사용량이 많아서 서버를 확장해야하는 경우가 발생해도 서비스를 중단하지 않고 확장할 수 있는 유연한 확장성을 얻는 다는 것이다.

