package com.sopt.dowadog.service.care;

import com.amazonaws.services.s3.model.Stats;
import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.Care;
import com.sopt.dowadog.repository.CareRepository;
import com.sopt.dowadog.util.ResponseMessage;
import com.sopt.dowadog.util.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service("careSignUpService")
public class SignUpService {

    private final CareRepository careRepository;

    public SignUpService(final CareRepository careRepository){
        this.careRepository = careRepository;

    }

    @Transactional
    public DefaultRes createCare(final Care care){

        try{
            if(!Optional.ofNullable(care).isPresent()){
                return DefaultRes.res(StatusCode.BAD_REQUEST,ResponseMessage.BAD_REQUEST);
            }

            careRepository.save(care);
            log.info(care.getAddress());


        }catch (Exception e){
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);

        }

        return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_CARE);
    }


    //보호소 아이디 중복 조회
    public DefaultRes readCareId(final String careId){


        try{
            if(careId.equals(null)){

                //아이디가 비어서 들어올 경우
                return DefaultRes.res(StatusCode.BAD_REQUEST,ResponseMessage.BAD_REQUEST);
            }
            // 유저 있는 지 유무 조사
            if(careRepository.findByCareUserId(careId).isPresent()){

                return DefaultRes.res(StatusCode.OK, ResponseMessage.UNABLE_USER);

            }else{

                return DefaultRes.res(StatusCode.OK, ResponseMessage.ABLE_USER);

            }


        }catch (Exception e){
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

}
