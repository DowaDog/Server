//package com.sopt.dowadog.controller.phoneverification;
//
//import com.sopt.dowadog.controller.phoneverification.requests.PhoneVerificationStartRequest;
//import com.sopt.dowadog.controller.phoneverification.requests.PhoneVerificationVerifyRequest;
//import com.sopt.dowadog.service.PhoneVerificationService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpSession;
//import javax.validation.Valid;
//
//@RestController
//@RequestMapping("api/phoneverify")
//public class PhoneVerificationController {
//
//    private PhoneVerificationService phoneVerificationService;
//
//    @Autowired
//    public PhoneVerificationController(PhoneVerificationService phoneVerificationService) {
//        this.phoneVerificationService = phoneVerificationService;
//    }
//
//    @RequestMapping(path = "start", method = RequestMethod.POST,
//            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public ResponseEntity start(@Valid @RequestBody PhoneVerificationStartRequest requestBody) {
//        return runWithCatch(() -> {
//            phoneVerificationService.start(requestBody.getCountryCode(),
//                    requestBody.getPhoneNumber(),
//                    requestBody.getVia());
//        });
//    }
//
//    @RequestMapping(path = "verify", method = RequestMethod.POST)
//    public ResponseEntity verify(@Valid @RequestBody PhoneVerificationVerifyRequest requestBody,
//                                 HttpSession session) {
//        return runWithCatch(() -> {
//            phoneVerificationService.verify(requestBody.getCountryCode(),
//                    requestBody.getPhoneNumber(),
//                    requestBody.getToken());
//            session.setAttribute("ph_verified", true);
//        });
//    }
//}
//
//
//}
