package com.skill_up.poll.controller;

import com.skill_up.poll.libs.Msg;
import com.skill_up.poll.service.RegistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;

@Controller
public class LoginController {

    private HttpSession session;

    @Autowired
    LoginController(HttpSession session){
        this.session = session;
    }

    @RequestMapping("/login")
    public String loginForm(Model model){
        Msg msg = new Msg(session);
        HashMap<String, ArrayList<String>> msgsWithTypeList = msg.getSessionAndFlush(Msg.SESSION_NAME);
        model.addAttribute("msgsWithTypeList", msgsWithTypeList);
        return "/login";

    }

    @GetMapping(value="/login",params="failure")
    public String loginFail(Model model){
        Msg msg = new Msg(session);
        msg.push(Msg.INFO, "ログインに失敗しました。");
        return "redirect:/login";

    }

}
