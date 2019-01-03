package com.sopt.dowadog.service;

import com.sopt.dowadog.model.common.LoginReq;
import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;



    public AuthService(final UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
    }

    public boolean loginCheck(LoginReq loginReq) {
//        String hashedPassword = DigestUtils.sha256Hex(loginReq.getPassword());
        String hashedPassword = loginReq.getPassword();
        final User user = userRepository.findByIdAndPassword(loginReq.getId(), hashedPassword);

        if (user != null) return true;

        return false;
    }

    public boolean idCheck(String userId) {

        if(userRepository.findById(userId).isPresent()) {
            return true;
        }
        return false;
    }

}
