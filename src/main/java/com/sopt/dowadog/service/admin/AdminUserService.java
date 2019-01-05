package com.sopt.dowadog.service.admin;

import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kohen.kang on 2019-01-04..
 */
@Service
public class AdminUserService {
    @Autowired
    UserRepository userRepository;

    public User readUserById(String userId) throws Exception{


        return userRepository.findById(userId).get();
    }

    public List<User> allUser() {
        return userRepository.findAll();
    }
}
