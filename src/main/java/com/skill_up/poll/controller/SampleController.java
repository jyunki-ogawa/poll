package com.skill_up.poll.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SampleController{
    @GetMapping("/displaysample")
    //リクエストに対する処理をメソッドで記載
    public String displaySample(Model model){
        model.addAttribute("fullName","埼玉次郎");
        return "sample";
    }
}
