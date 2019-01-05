package com.sopt.dowadog.controller.admin;

import com.sopt.dowadog.model.domain.Community;
import com.sopt.dowadog.model.domain.User;
import com.sopt.dowadog.repository.UserRepository;
import com.sopt.dowadog.service.CommunityService;
import com.sopt.dowadog.service.UserService;
import com.sopt.dowadog.service.admin.AdminCommunityService;
import com.sopt.dowadog.service.admin.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by kohen.kang on 2019-01-04..
 */

@RequestMapping("api/admin/community")
@Controller
public class AdminCommunityController {

    @Autowired
    AdminCommunityService adminCommunityService;

    @Autowired
    AdminUserService adminUserService;



    @PostMapping
    public ResponseEntity createCommunity(Community community, @RequestParam("userId") String userId) {
        try {
            adminCommunityService.createCommunity(userId, community);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("userList")
    @ResponseBody
    public List<User> userList() {
        return adminUserService.allUser();
    }



}
