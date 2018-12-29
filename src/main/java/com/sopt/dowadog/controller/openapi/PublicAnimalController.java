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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("openapi/animal")
public class PublicAnimalController {

    @Autowired
    private PublicAnimalService publicAnimalService;

    @GetMapping
    public ResponseEntity readPublicAnimalList(@ModelAttribute PublicAnimalSearchDto search,
                                               @RequestParam(name="page", defaultValue="0",required=false)int page,
                                               @RequestParam(name="limit", defaultValue="10", required=false)int limit) {

        return new ResponseEntity(publicAnimalService.readPublicAnimalList(search, page, limit), HttpStatus.OK);
    }

}
