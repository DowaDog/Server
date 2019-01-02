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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class SignUpService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    FileService fileService;

    @Value("${uploadpath.user}")
    private String baseDir;

    //회원가입
    @Transactional
    public DefaultRes<User> newUser(User user) {

        MultipartFile profileImgFile = user.getProfileImgFile();

        if(user.getProfileImgFile() != null){

            String filePath = new StringBuilder(baseDir).
                    append(S3Util.getUuid()).
                    append(profileImgFile.getOriginalFilename()).toString();

            fileService.fileUpload(profileImgFile, filePath);

            user.setProfileImgFile(profileImgFile);
            user.setProfileImg(filePath);
        }

        return DefaultRes.res(StatusCode.OK, ResponseMessage.CREATED_USER, userRepository.save(user));
    }

    //아이디 중복체크
    public DefaultRes duplicateUserId(String id){

        if(userRepository.findById(id).isPresent()){
            return DefaultRes.res(StatusCode.OK, ResponseMessage.UNABLE_USER, true);
        }else{
            return DefaultRes.res(StatusCode.OK, ResponseMessage.ABLE_USER, false);
        }
    }

    //이메일 중복체크
    public DefaultRes duplicateUserEmail(String email){
        if(userRepository.findByEmail(email).isPresent()){
            return DefaultRes.res(StatusCode.OK, ResponseMessage.UNABLE_EMAIL, true);
        }else{
            return DefaultRes.res(StatusCode.OK, ResponseMessage.ABLE_EMAIL, false);
        }
    }
}
