package com.skill_up.poll.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AccessDeniedController {

    @RequestMapping("/displayaccessdenied")
    public String accessDenied(){

        return "accessDenial";

    }

}
