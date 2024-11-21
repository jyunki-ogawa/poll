package com.skill_up.poll.controller;

import com.skill_up.poll.model.UserModel;
import com.skill_up.poll.service.RegistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.skill_up.poll.libs.Msg;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

@Controller
public class RegisterController {

    //サービスオブジェクトのように固定のものはfinalをつけておく
//    private final AuthService authService;
    private RegistService RegistService;
    private HttpSession session;

    @Autowired
    RegisterController(RegistService RegistService, HttpSession session){
        this.RegistService = RegistService;
        this.session = session;
    }

    @GetMapping("/register")
    String register() {
        return "register";
    }

    @PostMapping("/register")
    //リクエストに対する処理をメソッドで記載
    String addUser(
            @RequestParam("username") String username,
            @RequestParam("nickname") String nickname,
            @RequestParam("role") String role,
            @RequestParam("password") String password,
            Model model) {
        UserModel user = new UserModel(
                "",
                "",
                nickname,
                password,
                role,
                0,
               "",
               ""
        );

        user.setUsername(username);
        user.setNickname(nickname);
        user.setRole(role);
        user.setPassword(password);

        Msg msg = new Msg(session);

        try {

            RegistService.regist(user);

        } catch (Exception e) {

            msg.push(Msg.INFO, "ユーザー登録に失敗しました");
            return "redirect:/";

        }

        msg.push(Msg.INFO, user.getNickname() + "さん、ようこそ。");
        return "redirect:/login";
    }
}