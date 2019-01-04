package com.sopt.dowadog.controller.admin;

import com.sopt.dowadog.model.domain.Care;
import com.sopt.dowadog.service.admin.AdminCareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by kohen.kang on 2019-01-04..
 */

@RequestMapping("api/admin/care")
@Controller
public class AdminCareController {

    @Autowired
    AdminCareService adminCareService;

    @GetMapping
    @ResponseBody
    public List<Care> allCare() {
        return adminCareService.readAllCare();
    }

    @PostMapping
    public ResponseEntity createCare(@RequestBody Care care){
        adminCareService.createCare(care);

        return new ResponseEntity(HttpStatus.CREATED);
    }
}
