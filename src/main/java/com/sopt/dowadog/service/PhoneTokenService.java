//package com.sopt.dowadog.service;
//
//import com.authy.AuthyApiClient;
//import com.authy.OneTouchException;
//import com.authy.api.ApprovalRequestParams;
//import com.authy.api.Hash;
//import com.authy.api.OneTouchResponse;
//import com.authy.api.Token;
//import com.sopt.dowadog.controller.phoneverification.requests.VerifyTokenRequest;
//import com.sopt.dowadog.exceptions.TokenVerificationException;
//import com.sopt.dowadog.model.domain.User;
//import com.sopt.dowadog.repository.UserRepository;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//public class PhoneTokenService {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(PhoneTokenService.class);
//
//    private AuthyApiClient authyClient;
//    private UserRepository userRepository;
//
//    @Autowired
//    public TokenService(AuthyApiClient authyClient, UserRepository userRepository) {
//        this.authyClient = authyClient;
//        this.userRepository = userRepository;
//    }
//
//
//    public void sendSmsToken(String username) {
//        Hash hash = authyClient
//                .getUsers()
//                .requestSms(getUserAuthyId(username));
//
//        if(!hash.isOk()) {
//            logAndThrow("Problem sending token over SMS. " + hash.getMessage());
//        }
//    }
//
//    public void sendVoiceToken(String username) {
//        User user = userRepository.findFirstByName(username);
//
//        Hash hash = authyClient.getUsers().requestCall(user.getAuthyId());
//        if(!hash.isOk()) {
//            logAndThrow("Problem sending the token on a call. " + hash.getMessage());
//        }
//    }
//
//    public String sendOneTouchToken(String username) {
//        User user = userRepository.findFirstByName(username);
//
//        try {
//            ApprovalRequestParams params = new ApprovalRequestParams
//                    .Builder(user.getAuthyId(), "Login requested for Account Security account.")
//                    .setSecondsToExpire(120L)
//                    .addDetail("Authy ID", user.getAuthyId().toString())
//                    .addDetail("Username", user.getName())
//                    .addDetail("Location", "San Francisco, CA")
//                    .addDetail("Reason", "Demo by Account Security")
//                    .build();
//            OneTouchResponse response = authyClient
//                    .getOneTouch()
//                    .sendApprovalRequest(params);
//
//            if(!response.isSuccess()) {
//                logAndThrow("Problem sending the token with OneTouch");
//            }
//            return response.getApprovalRequest().getUUID();
//        } catch (OneTouchException e) {
//            logAndThrow("Problem sending the token with OneTouch: " + e.getMessage());
//        }
//        return null;
//    }
//
//    public void verify(String username, VerifyTokenRequest requestBody) {
//        Token token = authyClient
//                .getTokens()
//                .verify(getUserAuthyId(username), requestBody.getToken());
//
//        if(!token.isOk()) {
//            logAndThrow("Token verification failed. " + token.getError().toString());
//        }
//    }
//
//    public boolean retrieveOneTouchStatus(String uuid) {
//        try {
//            return authyClient
//                    .getOneTouch()
//                    .getApprovalRequestStatus(uuid)
//                    .getApprovalRequest()
//                    .getStatus()
//                    .equals("approved");
//        } catch (OneTouchException e) {
//            logAndThrow(e.getMessage());
//        }
//        return false;
//    }
//
//    private void logAndThrow(String message) {
//        LOGGER.warn(message);
//        throw new TokenVerificationException(message);
//    }
//
//    private Integer getUserAuthyId(String username) {
//        User user = userRepository.findFirstByName(username);
//        return user.getAuthyId();
//    }
//}
//
//}
