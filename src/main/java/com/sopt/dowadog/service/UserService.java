package com.sopt.dowadog.service;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.repository.UserRepository;
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
    JwtService jwtService;

    public DefaultRes<List<User>> readUserList() {
        final List<User> userList = userRepository.findAll();

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER, userList);
    }



    public User getUserByJwtToken(String jwtToken) throws Exception {

        String userId = jwtService.decode(jwtToken);

        if(userId == null) return null;

        if(userRepository.findById(userId).isPresent()) return userRepository.findById(userId).get();

        return null;
    }

    public List<User> readUserListOrdinary() {
        return userRepository.findAll();
    }

    public String a(){
        return null;
     }
}
