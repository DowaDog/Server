package com.sopt.dowadog.controller.api.care;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.Care;
import com.sopt.dowadog.model.dto.RegistrationMaterialDto;
import com.sopt.dowadog.service.care.CareRegistrationService;
import com.sopt.dowadog.service.care.CareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/care/registrations")
@Controller
public class CareRegistrationController {

    @Autowired
    CareRegistrationService careRegistrationService;

    @Autowired
    CareService careService;


    @GetMapping("{registrationId}")
    public ResponseEntity read(@RequestHeader(value = "Authorization", required = false) final String jwtToken,
                               @PathVariable(name = "registrationId")int registrationId) {
        try{
            Care care = careService.getCareByJwtToken(jwtToken);
            return new ResponseEntity(careRegistrationService.readRegistrationById(care, registrationId), HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(DefaultRes.UNAUTHORIZATION, HttpStatus.UNAUTHORIZED);
        }
    }


    //    신청서 전달후 대기상태  step0

    @PutMapping("{registrationId}/acceptStepZero")
    public ResponseEntity acceptStepZero(@RequestHeader(value = "Authorization", required = false) final String jwtToken,
                                         @PathVariable(name = "registrationId")int registrationId) {
        try{
            Care care = careService.getCareByJwtToken(jwtToken);

            return new ResponseEntity(careRegistrationService.updateRegistrationAcceptStepZero(care, registrationId), HttpStatus.OK);

            //service
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(DefaultRes.UNAUTHORIZATION, HttpStatus.UNAUTHORIZED);
        }
    }




    @PutMapping("{registrationId}/denyStepZero")
    public ResponseEntity denyStepZero(@RequestHeader(value = "Authorization", required = false) final String jwtToken,
                                       @PathVariable(name = "registrationId")int registrationId) {
        try{
            Care care = careService.getCareByJwtToken(jwtToken);

            return new ResponseEntity(careRegistrationService.updateRegistrationDenyStepZero(care, registrationId), HttpStatus.OK);

            //service
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(DefaultRes.UNAUTHORIZATION, HttpStatus.UNAUTHORIZED);
        }
    }
    // 신청서 전달에 대한 응답 끝




    // 전화완료 대기상태   step1

    @PutMapping("{registrationId}/acceptStepOne")
    public ResponseEntity acceptStepOne(@RequestHeader(value = "Authorization", required = false) final String jwtToken,
                                        @RequestBody RegistrationMaterialDto registrationMaterialDto,
                                        @PathVariable(name = "registrationId")int registrationId) {
        try{
            Care care = careService.getCareByJwtToken(jwtToken);

            return new ResponseEntity(careRegistrationService.updateRegistrationAcceptStepOne(care, registrationMaterialDto, registrationId), HttpStatus.OK);

            //service
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(DefaultRes.UNAUTHORIZATION, HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("{registrationId}/denyStepOne")
    public ResponseEntity denyStepOne(@RequestHeader(value = "Authorization", required = false) final String jwtToken,
                                      @PathVariable(name = "registrationId")int registrationId) {
        try{
            Care care = careService.getCareByJwtToken(jwtToken);

            return new ResponseEntity(careRegistrationService.updateRegistrationDenyStepOne(care, registrationId), HttpStatus.OK);

            //service
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(DefaultRes.UNAUTHORIZATION, HttpStatus.UNAUTHORIZED);
        }
    }

    //전화 끝후 결정완료




    // 보호소 방문 대기  step2

    @PutMapping("{registrationId}/acceptStepTwo")
    public ResponseEntity acceptStepTwo(@RequestHeader(value = "Authorization", required = false) final String jwtToken,
                                        @PathVariable(name = "registrationId")int registrationId) {
        try{
            Care care = careService.getCareByJwtToken(jwtToken);

            return new ResponseEntity(careRegistrationService.updateRegistrationAcceptStepTwo(care,registrationId), HttpStatus.OK);

            //service
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(DefaultRes.UNAUTHORIZATION, HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("{registrationId}/denyStepTwo")
    public ResponseEntity denyStepTwo(@RequestHeader(value = "Authorization", required = false) final String jwtToken,
                                      @PathVariable(name = "registrationId")int registrationId) {
        try{
            Care care = careService.getCareByJwtToken(jwtToken);

            return new ResponseEntity(careRegistrationService.updateRegistrationDenyStepTwo(care, registrationId), HttpStatus.OK);

            //service
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(DefaultRes.UNAUTHORIZATION, HttpStatus.UNAUTHORIZED);
        }
    }

    // 보호소 방문 완료





    //입양조건 완료 대기    complete

    @PutMapping("{registrationId}/acceptFinal")
    public ResponseEntity acceptStepFinal(@RequestHeader(value = "Authorization", required = false) final String jwtToken,
                                        @PathVariable(name = "registrationId")int registrationId) {
        try{
            Care care = careService.getCareByJwtToken(jwtToken);

            return new ResponseEntity(careRegistrationService.updateRegistrationAcceptComplete(care,registrationId), HttpStatus.OK);

            //service
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(DefaultRes.UNAUTHORIZATION, HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("{registrationId}/denyFinal")
    public ResponseEntity denyStepFinal(@RequestHeader(value = "Authorization", required = false) final String jwtToken,
                                      @PathVariable(name = "registrationId")int registrationId) {
        try{
            Care care = careService.getCareByJwtToken(jwtToken);

            return new ResponseEntity(careRegistrationService.updateRegistrationDenyComplete(care, registrationId), HttpStatus.OK);

            //service
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(DefaultRes.UNAUTHORIZATION, HttpStatus.UNAUTHORIZED);
        }
    }

    //입양조건 결정 완료

}
