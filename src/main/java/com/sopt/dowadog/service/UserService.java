package com.sopt.dowadog.service;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.repository.UserRepository;
import com.sopt.dowadog.util.ResponseMessage;
import com.sopt.dowadog.util.S3Util;
import com.sopt.dowadog.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Id;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public DefaultRes<List<User>> readUserList() {
        final List<User> userList = userRepository.findAll();

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER, userList);
    }

    public List<User> readUserListOrdinary() {
        return userRepository.findAll();
    }

    public String a(){
        return null;
     }
}
