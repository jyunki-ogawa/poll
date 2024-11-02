package com.skill_up.poll.model;

import com.skill_up.poll.libs.Msg;
import com.skill_up.poll.repository.JdbcTopicRepository;
import com.skill_up.poll.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;

@Component
public class CommentModel {

    public HttpSession session;

    @Autowired
    public CommentModel(HttpSession session){
        this.session = session;
    }

    public CommentModel(){
    }

    private int id;
    private int topic_id;
    private String user_id;
    private int del_flg;
    private String body;
    private String agree;
    private String nickname;

    public CommentModel(int id, int topic_id, String user_id, int del_flg, String body, String agree, String nickname) {
        this.id = id;
        this.topic_id = topic_id;
        this.user_id = user_id;
        this.del_flg = del_flg;
        this.body = body;
        this.agree = agree;
        this.nickname = nickname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(int topic_id) {
        this.topic_id = topic_id;
    }

    public int validateTopicId(int val) {
        return TopicModel.validateId(val);
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getDel_flg() {
        return del_flg;
    }

    public void setDel_flg(int del_flg) {
        this.del_flg = del_flg;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int validateBody(String val)
    {
        Msg msg = new Msg(session);
        int res = 1;

        if (val.length() > 100) {

            //コメントは100文字以内で入力してください。
            msg.push(Msg.INFO, "コメントは100文字以内で入力してください。");
            res = 0;

        }

        return res;
    }

    public String getAgree() {
        return agree;
    }

    public void setAgree(String agree) {
        this.agree = agree;
    }

    public int validateAgree(int val)
    {
        Msg msg = new Msg(session);
        int res = 1;

        // publishedが0、または1以外の時
        if (!(val == 0 || val == 1)) {
            //賛成か反対、どちらかの値を選択してください。
            msg.push(Msg.INFO, "賛成か反対、どちらかの値を選択してください。");
            res = 0;
        }

        return res;

    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}
