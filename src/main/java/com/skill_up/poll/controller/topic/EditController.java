package com.skill_up.poll.controller.topic;

import com.skill_up.poll.libs.Msg;
import com.skill_up.poll.model.TopicModel;
import com.skill_up.poll.model.TopicModelWithNickname;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;

@Controller
public class EditController {

    private AuthService authService;
    private JdbcTopicRepository jdbcTopicRepository;
    private HttpSession session;

    @Autowired
    EditController(AuthService authService, JdbcTopicRepository jdbcTopicRepository, HttpSession session){
        this.authService = authService;
        this.jdbcTopicRepository = jdbcTopicRepository;
        this.session = session;
    }

    @GetMapping("/topic/edit")
    public String getEdit(Model model, @RequestParam("topic_id") int topicId){

        String userId;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userId = ((UserDetails)principal).getUsername();
        } else {
            userId = principal.toString();
        }

        if(!authService.requirePermission(topicId, userId)){

            //topicの編集権限がない場合、トップページにリダイレクトする
            return "redirect:/";

        }

        TopicModelWithNickname topic = jdbcTopicRepository.fetchById(topicId);
        boolean isEdit = true;
        String url = "/topic/edit";

        model.addAttribute("topic", topic);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("url", url);

        return "/topic/edit";


    }

    @PostMapping("/topic/edit")
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

        if (!authService.requirePermission(topicId, userId)) {

            //topicの編集権限がない場合、トップページにリダイレクトする
            return "redirect:/";

        }

        TopicModel topicModel = new TopicModel();

        if (topicModel.validateId(topicId)
                * topicModel.validateTitle(title)
                * topicModel.validatePublished(published) == 0) {
            //トピックの更新に失敗しました。
            msg.push(Msg.INFO, "トピックの更新に失敗しました。");
            HashMap<String, ArrayList<String>> msgsWithTypeList = msg.getSessionAndFlush(Msg.SESSION_NAME);
            model.addAttribute("msgsWithTypeList", msgsWithTypeList);
            return "/topic/edit?topic_id=" + topicId;
        }

        try {

            jdbcTopicRepository.update(topicId,title,published);
            //トピックの更新に成功しました。
            msg.push(Msg.INFO, "トピックの更新に成功しました。");

        } catch(DataAccessException e) {

            //トピックの更新に失敗しました。
            msg.push(Msg.INFO, "トピックの更新に失敗しました。");
            HashMap<String, ArrayList<String>> msgsWithTypeList = msg.getSessionAndFlush(Msg.SESSION_NAME);
            model.addAttribute("msgsWithTypeList", msgsWithTypeList);
            return "/topic/edit?topic_id=" + topicId;

        }

        return "redirect:/topic/archive";

    }

}
