package com.sopt.dowadog.service.common;

import com.sopt.dowadog.model.common.LoginReq;
import com.sopt.dowadog.model.domain.Care;
import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.repository.CareRepository;
import com.sopt.dowadog.repository.UserRepository;
import com.sopt.dowadog.util.SHA256Util;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    private final CareRepository careRepository;

    public AuthService(final UserRepository userRepository, final CareRepository careRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.careRepository = careRepository;
    }

    public boolean loginCheck(LoginReq loginReq, String type) {

        SHA256Util sha256Util = new SHA256Util();
        String hashedPassword = sha256Util.SHA256Util(loginReq.getPassword());



        if (type.equals("normal")) {
            final User user = userRepository.findByIdAndPassword(loginReq.getId(), hashedPassword);
            if(user != null) return true;
        }

        if (type.equals("care")) {
            final Care care = careRepository.findByCareUserIdAndPassword(loginReq.getId(), hashedPassword);
            if(care != null) return true;
        }

//        String hashedPassword = DigestUtils.sha256Hex(loginReq.getPassword());


        return false;
    }

    public boolean idCheck(String userId, String type) {

        if (type.equals("normal")) {
            if (userRepository.findById(userId).isPresent()) {
                return true;
            }
        }
        if (type.equals("care")) {
            if (careRepository.findByCareUserId(userId).isPresent()) {
                return true;
            }
        }
        return false;
    }

}
