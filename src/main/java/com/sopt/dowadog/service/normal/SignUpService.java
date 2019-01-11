package com.sopt.dowadog.service.normal;

import com.sopt.dowadog.model.common.DefaultRes;
import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.model.dto.SignupFormDto;
import com.sopt.dowadog.repository.UserRepository;
import com.sopt.dowadog.service.common.FileService;
import com.sopt.dowadog.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SignUpService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    FileService fileService;

    @Value("${uploadpath.user}")
    private String baseDir;

    @Value("${PASSWORD.KEY}")
    private String pwdKey;

    //회원가입
    @Transactional
    public DefaultRes<SignupFormDto> newUser(SignupFormDto signupFormDto, MultipartFile profileImgFile) {


        try{

            User user = User.setUserBySignupDto(signupFormDto);
            String pw = user.getPassword();



            //단방향 암호화
            SHA256Util sha256Util = new SHA256Util();
            String newPw = sha256Util.SHA256Util(pw);
            user.setPassword(newPw);


            //양방향 암호화
            AES256Util aes256Util = new AES256Util(pwdKey);


            String email = aes256Util.aesEncode(user.getEmail());
            String name = aes256Util.aesEncode(user.getName());
            String phone = aes256Util.aesEncode(user.getPhone());

            user.setEmail(email);
            user.setName(name);
            user.setPhone(phone);


            if(profileImgFile != null) {
                String filePath = new StringBuilder(baseDir).
                        append(S3Util.getUuid()).
                        append(profileImgFile.getOriginalFilename()).toString();

                fileService.fileUpload(profileImgFile, filePath);
                user.setProfileImg(filePath);
            }else{
                // 이미지 널일때 분기 처리
                user.setProfileImg(new StringBuilder(baseDir).
                        append("profile_default_img.png")
                        .toString());

            }

            userRepository.save(user);

            return DefaultRes.res(StatusCode.OK, ResponseMessage.CREATED_USER);

        }catch (Exception e){
            e.printStackTrace();

            return DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR,ResponseMessage.INTERNAL_SERVER_ERROR);
        }

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
        try{
            AES256Util aes256Util = new AES256Util(pwdKey);

            System.out.println(email);
            System.out.println(aes256Util.aesEncode(email));


            if(userRepository.findByEmail(aes256Util.aesEncode(email)).isPresent()){
                return DefaultRes.res(StatusCode.OK, ResponseMessage.UNABLE_EMAIL, true);
            }else{
                return DefaultRes.res(StatusCode.OK, ResponseMessage.ABLE_EMAIL, false);
            }

        }catch (Exception e){
            return DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR,ResponseMessage.INTERNAL_SERVER_ERROR);


        }

    }
}
