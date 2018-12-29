package com.sopt.dowadog.controller.openapi;

import com.sopt.dowadog.model.dto.PublicAnimalSearchDto;
import com.sopt.dowadog.service.PublicAnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("openapi/animal")
public class PublicAnimalController {

    @Autowired
    private PublicAnimalService publicAnimalService;

    @GetMapping
    public ResponseEntity readPublicAnimalList(@ModelAttribute PublicAnimalSearchDto search) {

        return new ResponseEntity(publicAnimalService.readPublicAnimalList(search), HttpStatus.OK);
    }

}
