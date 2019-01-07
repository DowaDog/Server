package com.sopt.dowadog.scheduler;

import com.sopt.dowadog.model.domain.Mailbox;
import com.sopt.dowadog.model.domain.MailboxReservation;
import com.sopt.dowadog.repository.MailboxRepository;
import com.sopt.dowadog.repository.MailboxReservationRepository;
import com.sopt.dowadog.util.AsyncUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReservationScheduler {
    @Autowired
    MailboxReservationRepository mailboxReservationRepository;
    @Autowired
    MailboxRepository mailboxRepository;


    /*
    * 레절베이션테이블 탐색(데이트랑 오늘 날짜 비교 -> 같으면 같은 것에 대해서 푸쉬 알람을 쏴준다 (다중 푸쉬)
    * ,on을 off로 바꿔주고 off에 있는 것을 off에 있는 것을 mailbox 에 넣어주기 )*/


    @Transactional
    @Scheduled(cron = "0 0 9 * * *")
    //@Scheduled(fixedDelay = 10000000)//시간은 밀리세컨트
    public void setMailboxData(){

        try{
            System.out.println("------예약 알람에 대한 스케쥴러 작동--------");
            AsyncUtil asyncUtil = new AsyncUtil();
            List<String> medicalArr = new ArrayList<>();
            List<String> photoArr = new ArrayList<>();


            //On인 것들과 오늘인 것들 모두 불러와서!
            List<MailboxReservation> mailboxReservationList = mailboxReservationRepository.findAllByStateEqualsAndTimeEquals("on", LocalDate.now());
            System.out.println("on이면서 오늘 것"+mailboxReservationList.size());

            if(mailboxReservationList.size()!=0) {


                for (MailboxReservation temp : mailboxReservationList) {



                    // 메디컬이면서 토큰이 있는 유저들
                    if (temp.getUser().getDeviceToken() != null && temp.getType().equals("medical")) {
                        medicalArr.add(temp.getUser().getDeviceToken());
                    }


                    System.out.println("메디컬인 토큰이 있는 유저들" + medicalArr.size());


                    //사진이면서 토큰이 있는 유저들
                    if (temp.getUser().getDeviceToken() != null && temp.getType().equals("photo")) {
                        photoArr.add(temp.getUser().getDeviceToken());

                    }

                    Mailbox mailbox = Mailbox.builder()
                            .user(temp.getUser())
                            .title(temp.getTitle())
                            .detail(temp.getDetail())
                            .type(temp.getType())
                            .complete(false)
                            .build();


                    MailboxReservation mailboxReservation = MailboxReservation.builder()
                            .id(temp.getId())
                            .state("off")
                            .type(temp.getType())
                            .title(temp.getTitle())
                            .detail(temp.getDetail())
                            .time(temp.getTime())
                            .user(temp.getUser())
                            .build();
                    mailboxRepository.save(mailbox);//우체통에 옮기기
                    mailboxReservationRepository.save(mailboxReservation);//우체통 예약발송에 있는 거 업데이트 하기


                }
                //푸시알림 호출
                if(medicalArr.size()>0){
                    asyncUtil.send(medicalArr, "검사와 접종은 셨나요?", "입양 후 필수적인 검진과 접종을 마쳐주세요!");
                    System.out.println("메디컬메디컬메디컬알람알람");

                }
                if(photoArr.size()>0){
                    asyncUtil.send(photoArr, "이달의 사진을 커뮤니티에 게시해주세요!", "");
                    System.out.println("사진사진알람알람");
                }

            }

        }catch (Exception e){
            e.printStackTrace();

        }

    }

}
