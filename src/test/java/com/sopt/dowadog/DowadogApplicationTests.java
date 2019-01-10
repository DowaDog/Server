package com.sopt.dowadog;

import com.sopt.dowadog.util.AsyncUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DowadogApplicationTests {

    @Test
    public void contextLoads() {
        try{

            AsyncUtil asyncUtil = new AsyncUtil();
            System.out.println("#################### MESSAGE GO");
            asyncUtil.sendOne("cIegScLjZlI:APA91bE3ZXq2iy2N-P47N5F7NwBjN6fwY0y0jw3oulTL_Mh3WQacuSF0vy9zJl6VJDBpEaXPPZ0ldHWNSEMA1g1-0sU6AuqvkQ3FTLxaHetqA7m-gcUAvz-kqLjpsAzlDL-O9aGt-L2w", "정신이", "나갔었나봐");
            System.out.println("#################### MESSAGE END");

        } catch (Exception e){
            System.out.println(e);
        }
    }

}

