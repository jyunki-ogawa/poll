package com.skill_up.poll.controller;

import com.skill_up.poll.libs.Msg;
import com.skill_up.poll.model.TopicModelWithNickname;
import com.skill_up.poll.repository.JdbcTopicRepository;
import com.skill_up.poll.service.RegistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class HomeController {


    private JdbcTopicRepository jdbcTopicRepository;
    private HttpSession session;
    @Autowired
    HomeController(JdbcTopicRepository jdbcTopicRepository, HttpSession session){
        this.jdbcTopicRepository = jdbcTopicRepository;
        this.session = session;
    }



    @RequestMapping("/")
    public String home(Model model){

        List<TopicModelWithNickname> topicList = jdbcTopicRepository.fetchPublishedTopics();
        TopicModelWithNickname firstTopic;
        List<TopicModelWithNickname> resTopicList;
        boolean fromTopPage = true;
        String topicURL = "/topic/detail";
        boolean withStatus = false;

        if (topicList != null){
            if (topicList.size() < 2) {
                firstTopic = topicList.get(0);
                resTopicList = null;
            } else {
                firstTopic = topicList.get(0);
                resTopicList = topicList.subList(1,topicList.size());
            }
        } else {
            firstTopic = null;
            resTopicList = null;
        }

        model.addAttribute("firstTopic", firstTopic);
        model.addAttribute("resTopicList", resTopicList);
        model.addAttribute("fromTopPage", fromTopPage);
        model.addAttribute("topicURL", topicURL);
        model.addAttribute("withStatus", withStatus);
        Msg msg = new Msg(session);
        HashMap<String, ArrayList<String>> msgsWithType = msg.getSessionAndFlush(Msg.SESSION_NAME);
        model.addAttribute("msgsWithType", msgsWithType);

        return "home";

    }

}
