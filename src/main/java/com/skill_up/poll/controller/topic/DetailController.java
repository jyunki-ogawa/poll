package com.skill_up.poll.controller.topic;

import com.skill_up.poll.libs.Msg;
import com.skill_up.poll.model.CommentModel;
import com.skill_up.poll.model.TopicModelWithNickname;
import com.skill_up.poll.repository.JdbcCommentRepository;
import com.skill_up.poll.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.skill_up.poll.repository.JdbcTopicRepository;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class DetailController {

    private JdbcTopicRepository jdbcTopicRepository;
    private JdbcCommentRepository jdbcCommentRepository;
    private HttpSession session;

    @Autowired
    DetailController(JdbcTopicRepository jdbcTopicRepository, JdbcCommentRepository jdbcCommentRepository, HttpSession session){
        this.jdbcTopicRepository = jdbcTopicRepository;
        this.jdbcCommentRepository = jdbcCommentRepository;
        this.session = session;
    }

    @GetMapping("/topic/detail")
    public String getDetail(Model model,
                            RedirectAttributes redirectAttributes,
                            @RequestParam("topic_id") int topicId){

        boolean fromTopPage = false;
        boolean withStatus = true;
        Msg msg = new Msg(session);

        jdbcTopicRepository.incrementViewCount(topicId);

        TopicModelWithNickname topic = jdbcTopicRepository.fetchById(topicId);

        //topicがnull、もしくはpublishedが非公開の場合メッセージを表示し、トップページにリダイレクト
        if (topic == null || topic.getPublished() == 0){

            //topicが見つからなかった場合のメッセージ
            msg.push(Msg.INFO, "トピックが見つかりません。");
            HashMap<String, ArrayList<String>> msgsWithTypeList = msg.getSessionAndFlush(Msg.SESSION_NAME);
            model.addAttribute("msgsWithTypeList", msgsWithTypeList);
            //トップページにリダイレクトする
            return "redirect:/";
        }

        List<CommentModel> commentList = jdbcCommentRepository.fetchByTopicId(topicId);

        model.addAttribute("firstTopic", topic);
        model.addAttribute("commentList", commentList);
        model.addAttribute("fromTopPage", fromTopPage);
        model.addAttribute("withStatus", withStatus);

        HashMap<String, ArrayList<String>> msgsWithTypeList = msg.getSessionAndFlush(Msg.SESSION_NAME);
        model.addAttribute("msgsWithTypeList", msgsWithTypeList);

        return "topic/detail";

    }

    @Transactional
    @PostMapping("/topic/detail")
    public String postDetail(Model model,
                             RedirectAttributes redirectAttributes,
                             @RequestParam("topic_id") int topicId,
                             @RequestParam("agree") int agree,
                             @RequestParam("body") String body
                            ){

        String userId;
        Msg msg = new Msg(session);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userId = ((UserDetails) principal).getUsername();
        } else {
            userId = principal.toString();
        }
        CommentModel commentModel = new CommentModel(session);
        if (commentModel.validateTopicId(topicId)
                * commentModel.validateAgree(agree)
                * commentModel.validateBody(body) == 0) {

            msg.push(Msg.INFO, "回答に失敗しました。");

            redirectAttributes.addAttribute("topic_id", topicId);

            return "redirect:/topic/detail";
        }

        int result = 0;

        try {

            result = jdbcTopicRepository.incrementLikesOrDislikes(topicId, agree);

            if (result != 0 && !body.isEmpty()) {

                jdbcCommentRepository.insert(topicId, agree, body, userId);

            }

        } catch(DataAccessException e) {

            //エラーメッセージを表示
            msg.push(Msg.INFO, "回答に失敗しました。");
            msg.push(Msg.DEBUG, e.getMessage());
            result = 0;

        } finally {

            if(result == 1) {

                msg.push(Msg.INFO, "回答に成功しました。");

            } else {

                msg.push(Msg.INFO, "回答に失敗しました。");
            }

        }

        redirectAttributes.addAttribute("topic_id", topicId);
        return "redirect:/topic/detail";

    }

}
