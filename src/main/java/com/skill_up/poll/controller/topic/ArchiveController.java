package com.skill_up.poll.controller.topic;

import com.skill_up.poll.model.TopicModel;
import com.skill_up.poll.model.UserModel;
import com.skill_up.poll.repository.JdbcTopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

import org.springframework.ui.Model;

@Controller
public class ArchiveController {

    @Autowired
    private JdbcTopicRepository jdbcTopicRepository;

    @GetMapping("/topic/archive")
    String register(Model model) {

        String topicURL = "/topic/edit";
        boolean withStatus = true;

        //If文の中でuserIdを定義するとスコープ外となる。
        String userId;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userId = ((UserDetails)principal).getUsername();
        } else {
            userId = principal.toString();
        }

        List<TopicModel> topicList = jdbcTopicRepository.fetchByUserId(userId);
        model.addAttribute("topicList", topicList);
        model.addAttribute("topicURL", topicURL);
        model.addAttribute("withStatus", withStatus);
        return "/topic/archive";

    }


}
