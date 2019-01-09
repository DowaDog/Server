package com.sopt.dowadog.service.normal;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.Registration;
import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.repository.RegistrationRepository;
import com.sopt.dowadog.repository.UserRepository;
import com.sopt.dowadog.service.common.JwtService;
import com.sopt.dowadog.util.ResponseMessage;
import com.sopt.dowadog.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RegistrationRepository registrationRepository;

    @Autowired
    JwtService jwtService;

    public DefaultRes<List<User>> readUserList() {
        final List<User> userList = userRepository.findAll();

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER, userList);
    }

    public DefaultRes checkRegistration(User user) {
        if(registrationRepository.findByUserAndUserCheck(user, false).isPresent()){
            List<Registration> registrationList = registrationRepository.findByUserAndUserCheck(user, false).get();

            System.out.println("읽지않은 신청서 있음");
            for(Registration lastRegistration : registrationList) {
                lastRegistration.setUserCheck(true);
                registrationRepository.save(lastRegistration);
            }
        }
        return DefaultRes.res(StatusCode.OK, ResponseMessage.CHECK_REGISTRATION);
    }


    public User getUserByJwtToken(String jwtToken) {

        String userId = jwtService.decode(jwtToken);

        if(userId == null) return null;

        if(userRepository.findById(userId).isPresent()) return userRepository.findById(userId).get();

        return null;
    }



}
