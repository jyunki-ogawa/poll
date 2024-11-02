package com.skill_up.poll.service;

import com.skill_up.poll.libs.Msg;
import com.skill_up.poll.model.TopicModelWithNickname;
import com.skill_up.poll.repository.JdbcTopicRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JdbcTopicRepository jdbcTopicRepository;
    private HttpSession session;

    @Autowired
    public AuthService(JdbcTopicRepository jdbcTopicRepository, HttpSession session) {

        this.jdbcTopicRepository = jdbcTopicRepository;
        this.session = session;

    }

    public Integer hasPermission(int topicId, String userId) {

        return jdbcTopicRepository.isUserOwnTopic(topicId, userId);

    }

    public boolean requirePermission(int topicId, String userId) {
        Msg msg = new Msg(session);
        if(hasPermission(topicId, userId) == null) {

            msg.push(Msg.ERROR, "編集権限がありません。ログインして再度試してみてください。");

            return false;
        }

        return true;

    }

}
