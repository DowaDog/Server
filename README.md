# Project
SOPT 23rd Appjam 기다릴개 서버 프로젝트




## Project spec 정보

Springboot 2.1.1 RELEASE

자바 버전 : JAVA 8
ORM 기술 : Hibernate JPA





## 서버 정보

|             | 역할 |host                                            |
| ----------- |- |------------------------------------------------- |
| **aws ec2** | API 애플리케이션 서버 1 |ec2-user@54.180.42.254                                     |
|  | API 애플리케이션 서버 2 |ubuntu@13.209.185.163 |
| **aws rds** | DB 서버 |rsh.cpcceaqwm3sy.ap-northeast-2.rds.amazonaws.com |
| **aws s3** | 파일 스토리지 |https://s3.ap-northeast-2.amazonaws.com/ryudd/ |





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



#### 4. ec2에서 실시간 로그 보기

~~~cmd
tail -f "filename"
~~~







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





## Logback

**Logback**은 "자바 오픈소스 로깅 프레임워크"로 SLF4J의 구현체이자 **스프링 부트의 기본 로그** 객체다.

log4j, log4j2, JUL(java.util.logging)과 성능을 비교했을 때 logback은 훌륭한 성능을 보여준다.

그리고 결정적으로 자바 프로그램에서 로그를 사용할 때 가장 많이 사용되고 있기 때문에 알아두어야 한다.

출처: https://jeong-pro.tistory.com/154 [기본기를 쌓는 정아마추어 코딩블로그]



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

캐시 엔진 중의 하나이다. 주요 특징으로는 아래와 같다.

경량의 빠른 캐시 엔진
확장성 : 메모리, 디스크 저장 지원, 멀티 CPU의 동시 접근에 튜닝
분산 지원 : 동기, 비동기 복사, 피어 자동 발견
높은 품질

**왜 쓰는가?**

Java 메서드에 캐싱을 적용함으로써 캐시에 보관된 정보로 메서드의 실행 횟수를 줄여준다.
대상 메서드가 실행될 때마다 추상화가 해당 메서드가 같은 인자로 이미 실행되었는지 확인하는 캐싱 동작을 적용
해당 데이터가 존재한다면 실제 메서드를 실행하지 않고 결과를 반환하고 존재하지 않는다면 메서드를 실행하고 그 결과를 캐싱한 뒤에 사용자에게 반환해서 다음번 호출 시에 사용할 수 있게 한다. 이 방법을 통해 비용이 큰 메서드(CPU든 IO든) 해당 파라미터로는 딱 한 번만 실행
물론 이 접근방법은 얼마나 많이 호출하든지 간에 입력(혹은 인자)이 같으면 출력(결과)도 같다는 것을 보장하는 메서드에서만 동작

출처: http://sjh836.tistory.com/129 [빨간색코딩]

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


* spring 3.1에서 cache추상화를 시켜서 ehcache만 bean으로 등록하면 된다. (**EhcacheConfig.java**)
* defaultCache : 기본 캐시설정 (@Cacheable만 사용시) 
* snapshotData : 공공데이터 조회의 경우 스케줄러를 이용한 스냅샷 데이터이므로 매번 조회하는데 연산을 쓰지 않아도 됨. 이러한 부분에 리소스를 줄이기 위해 ehCache 적용
* 공공데이터 갱신에는 10000건 이하정도의 데이터 객체가 delete/insert 되므로 메모리에저장하는 객체 수 제한을 10000으로 정하였고, 5분의 갱신주기를 가지게끔 하였음. ( delete/insert 사이 과정에 의한 데이터 불일치 가능성때문에 큰 폭으로 설정하지 않음 )










# 어노테이션 추가적 사용 설명



##@EnableScheduling

Spring에서는 일정한 주기마다 작업을 실행할 수 있는 Schedule기능이 포함되어있다. Spring Batch만큼 순차작업이나 실패에 따른 복구등의 많은 기능을 가지고 있지 않지만, 간략한 설정과 어노테이션만으로 편리하게 설정이 가능한 장점을 가지고 있다. 최소한의 코드를 가진다는건 한눈에 파악할 수 있고, 빠르게 수정이 가능하다는 뜻이다.

스프링의 Schedule기능은 다음과 같이 구성할 수 있다. 우선 설정파일에 스케쥴을 사용하겠다는 의미로 @EnableScheduling을 추가하여 스케쥴링 기능을 사용하겠다는 것을 표기하여 줄 수 있다.

```java
@EnableScheduling
@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
```

그리고 `@Scheduled` 어노테이션을 메서드 상단에 붙여 메서드가 주기적으로 동작하도록 설정할 수 있다.



이러한 주기작업을 위해 다양한 옵션을 정의할 수 있다. cron식등을 활용하여 비정기적인 작업, 초기화시 딜레이 등 다양한 기능을 활용할 수 있다. 필요에 따라서는 아래와 같이 SpEL을 사용하여 프로퍼티의 값에 따른 주기적인 작업설정도 가능하다.






##@Async

#### - 스프링내에서 별도의 쓰레드에서 실행되면서 비동기로 실행하도록 한다.
#### - 이 프로젝트 내에서는 fcm을 사용하기 위해서 사용되었다.




### 조건

##### 1. public 메소드에서 적용

##### 2. 같은 클래스 안에서 async 메소드를 호출하는 셀프 호출의 경우 작동하지 않는다.

##### 3. 기본적으로 리턴 타입이 없는 메소드의 경우 void 로 처리하지만 리턴타입이 있는 경우 Future 객체에 실체 리턴값 넣어서 리턴타입이 있는 메소드로 적용함

##### 4. Application 클래스에 @EnableAsync를 추가해준다.









## JPA




### @OneToMany 와 @ManyToOne의 어노테이션 사용으로 인한 무한참조

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

### Nginx를 사용하게 된 경위

> 첫번째로 재배포의 문제가 발생했기 때문에 road-balancing을 고려해보게 되었다.
우리의 서비스를 제공하는 것에 있어서 A타입과 B타입 두가지로 나누어진 어플 서비스를 제공해야했고 당초 개발 상황보다 살짝 빠르게 어플 스토어에 어플이 올라가야했다. 서비스를 제공하는 것은 서비스가 중단되면 안되고, 사용자는 서비스를 지속적으로 제공받을 수 있어야하는데 우리는 추가적으로 개발된 기능들을 계속 재배포해야했다. 때문에 서비스를 제공할 수 있는 서버를 두대 이상두고 road-balancing 설정을 잡아두어 중단없는 재배포가 가능하게 만들어 두려는 목적으로 시도했다.

>두번째로 곤란을 겪고 있던 https를 Nginx를 road-balancer로 사용하면서 간단하게 처리할 수 있었다. spring의 내부 톰캣에 ssl 설정을 넣는 부분에서 혼란을 겪고 있었던 https를 Nginx에 적용시키고 Nginx를 road-balancer이자 proxy-server의 역할을 하도록 설정할 수 있었고, Nginx에 위에 서술된 것처럼 letsencrypt를 통해 인증서를 발급받고 적용시킨뒤 proxy 설정을 해두었다. 이렇게 되었을 때 Nginx 외장 프록시 서버 뒤에 우리의 WAS서버가 위치하는 형태의 설계로 proxy 서버로서도 Nginx를 사용하고 road-balancing 설정을 넣어주면서 road-balancer로도 Nginx를 활용할 수 있었기 때문에 Nginx를 고려했고, 실제로 Nginx에는 road-balancing 기능이 꽤나 편리하고 강력하게 구현되어 있기 때문에 도입하게 되었다.

### road-balancing
> Nginx는 road-balancing을 간단한 방법으로 제공해주는데 upstream이라는 옵션을 config파일 안에 넣어주는 것으로 설정할 수 있다. 
이러한 방식은 default로 Round-Robin 방식을 사용하여 road-balancing해주게 된다. 
upstream에 우리가 가지고 있는 분산할 서버의 도메인 두개를 넣어주고 각각 마다 weight나 기타 balancing 설정들을 넣어줄 수 있다. least_conn이라는 설정을 넣어줄 경우 현재 트래픽이 가장 적은 서버로 매핑해주는 것으로 보이는데 우리는 일단 기본 설정인 Round-Robin방식을 사용하였고, main, sub서버가 구분되어 있는 것이 아니기 때문에 따로 가중치를 추가적으로 주지는 않았다. 
upstream에 위의 설정을 잡아준 뒤 config 파일내의 server block에서 proxy_pass로 upstream 옵션이름을 지정해주는 것으로 설정을 마무리했고, road-balancing을 적용할 수 있었다.

### 로드밸런싱을 했을 때 얻을 수 있는 장점
1. 이렇게 서비스를 할 때 로드 밸런서를 두고 서버를 두개 놓고 서비스를 하게 되면 얻을 수 있는 가장 큰 이득은 중단 없는 재배포가 가능하다는 점이다.
   2대의 서버중 1대가 작동을 멈추어도 서비스를 온전히 계속 제공할 수 있으므로 재배포나 유지 보수부분에서 얻을 수 있는 것이 많다.
2. 저가의 서버를 여러대 이용해서 고가의 서버를 사용하는 것처럼 사용할 수 있다는 것도 장점이 될 수 있을 것이다.
3. 또한 혹여 추후 사용량이 많아서 서버를 확장해야하는 경우가 발생해도 서비스를 중단하지 않고 확장할 수 있는 유연한 확장성을 얻는 다는 것이다.





## 최종 인프라 다이어그램 모델 구상도



![image-20190112091543234](https://ryudd.s3.amazonaws.com/dowadog/img_all.png)

