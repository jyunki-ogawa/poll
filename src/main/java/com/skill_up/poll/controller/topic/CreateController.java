package com.skill_up.poll.controller.topic;

import com.skill_up.poll.libs.Msg;
import com.skill_up.poll.model.TopicModel;
import com.skill_up.poll.model.UserModel;
import com.skill_up.poll.repository.JdbcTopicRepository;
import com.skill_up.poll.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;

@Controller
public class CreateController {

    private AuthService authService;
    private JdbcTopicRepository jdbcTopicRepository;
    private HttpSession session;

    @Autowired
    CreateController(AuthService authService, JdbcTopicRepository jdbcTopicRepository, HttpSession session){
        this.authService = authService;
        this.jdbcTopicRepository = jdbcTopicRepository;
        this.session = session;
    }

    @GetMapping("/topic/create")
    public String getCreate(Model model){

        TopicModel topic = new TopicModel(0,"",1,0,0,0,"", 0);
        boolean isEdit = false;
        String url = "/create";

        model.addAttribute("topic", topic);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("url", url);

        //createとeditでviewは使い回し。
        return "/topic/edit";

    }

    @PostMapping("/topic/create")
    public String postEdit(Model model,
                           @RequestParam("topic_id") int topicId,
                           @RequestParam("title") String title,
                           @RequestParam("published") int published
    ) {

        String userId;
        Msg msg = new Msg(session);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userId = ((UserDetails) principal).getUsername();
        } else {
            userId = principal.toString();
        }
        UserModel userModel = new UserModel(session);
        TopicModel topicModel = new TopicModel();
        if (userModel.validateId(userId)
                * topicModel.validateTitle(title)
                * topicModel.validatePublished(published) == 0) {
            msg.push(Msg.INFO, "トピックの登録に失敗しました。");
            HashMap<String, ArrayList<String>> msgsWithTypeList = msg.getSessionAndFlush(Msg.SESSION_NAME);
            model.addAttribute("msgsWithTypeList", msgsWithTypeList);
            return "/topic/create";
        }

        try {

            jdbcTopicRepository.insert(title, published, userId);
            msg.push(Msg.INFO, "トピックの登録に成功しました。");

        } catch(DataAccessException e) {

            //トピックの登録に失敗しました。
            msg.push(Msg.INFO, "トピックの登録に失敗しました。");
            HashMap<String, ArrayList<String>> msgsWithTypeList = msg.getSessionAndFlush(Msg.SESSION_NAME);
            model.addAttribute("msgsWithTypeList", msgsWithTypeList);
            return "/topic/create";

        }

        HashMap<String, ArrayList<String>> msgsWithTypeList = msg.getSessionAndFlush(Msg.SESSION_NAME);
        model.addAttribute("msgsWithTypeList", msgsWithTypeList);
        return "redirect:/topic/archive";

    }
}
