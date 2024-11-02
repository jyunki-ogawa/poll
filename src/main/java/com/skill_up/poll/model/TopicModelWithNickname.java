package com.skill_up.poll.model;

public class TopicModelWithNickname {

    private int id;
    private String title;
    private int published;
    private int views;
    private int likes;
    private int dislikes;
    private String user_id;
    private int del_flg;
    private String nickname;

    public TopicModelWithNickname(int id, String title, int published, int views, int likes, int dislikes, String user_id, int del_flg, String nickname) {
        this.id = id;
        this.title = title;
        this.published = published;
        this.views = views;
        this.likes = likes;
        this.dislikes = dislikes;
        this.user_id = user_id;
        this.del_flg = del_flg;
        this.nickname = nickname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPublished() {
        return published;
    }

    public void setPublished(int published) {
        this.published = published;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}
