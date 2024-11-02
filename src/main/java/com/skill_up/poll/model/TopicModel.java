package com.skill_up.poll.model;

import com.skill_up.poll.libs.Msg;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

public class TopicModel {

    private int id;
    private String title;
    private int published;
    private int views;
    private int likes;
    private int dislikes;
    private String user_id;
    private int del_flg;
    public HttpSession session;

    @Autowired
    public TopicModel(HttpSession session){
        this.session = session;
    }
    public TopicModel(){}

    //DataClassRowMapperのためにコンストラクタを設定する
    public TopicModel(int id, String title, int published, int views, int likes, int dislikes, String user_id, int del_flg) {
        this.id = id;
        this.title = title;
        this.published = published;
        this.views = views;
        this.likes = likes;
        this.dislikes = dislikes;
        this.user_id = user_id;
        this.del_flg = del_flg;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static int validateId(int val) {
        int res = 1;

        //型によって数値であることは担保されているので、初期値の0出ないかチェックする
        if (val == 0) {

            //パラメーターが不正です。
            res = 0;

        }

        return res;

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int validateTitle(String val) {
        int res = 1;
        Msg msg = new Msg(session);

        if (val.isEmpty()) {

            //タイトルを入力してください。
            msg.push(Msg.INFO, "タイトルを入力してください。");
            res = 0;

        } else {

            if (val.length() > 30) {
                //タイトルは30文字以内で入力してください。
                msg.push(Msg.INFO, "タイトルは30文字以内で入力してください。");
                res = 0;
            }
        }

        return res;
    }


    public int getPublished() {
        return published;
    }

    public void setPublished(int published) {
        this.published = published;
    }

    public int validatePublished(int val) {
        int res = 1;
        Msg msg = new Msg(session);
        // 0、または1以外の時
        if (!(val == 0 || val == 1)) {

            msg.push(Msg.INFO, "公開ステータスが不正です。");
            res = 0;

        }

        return res;
    }


    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
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
}
