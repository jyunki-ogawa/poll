package com.skill_up.poll.model;

import com.skill_up.poll.libs.Msg;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class UserModel implements UserDetails {

    public HttpSession session;

    @Autowired
    public UserModel(HttpSession session){
        this.session = session;
    }

    public UserModel(){

    }

    private String id;
    private String username;
    private String nickname;
    private String password;
    private String role;
    private int del_flg;
    private String updated_by;
    private String updated_at;

    //DataClassRowMapperのためにコンストラクタを設定する
    public UserModel(String id,
                     String username,
                     String nickname,
                     String password,
                     String role,
                     int del_flg,
                     String updated_by,
                     String updated_at
                     ){

        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
        this.del_flg = del_flg;
        this.updated_by = updated_by;
        this.updated_at = updated_at;

    }




    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        return authorities;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public int validateId(String userId) {
        int res = 1;
        Msg msg = new Msg(session);

        if(userId.isEmpty()) {

            msg.push(Msg.ERROR, "ユーザーIDを入力してください。");
            res = 0;

        } else {

            if(userId.length() > 10) {

                //Msg::push(Msg::ERROR, 'ユーザーIDは１０桁以下で入力してください。');
                msg.push(Msg.ERROR, "ユーザーIDは１０桁以下で入力してください。");
                res = 0;
            }

        }

        return res;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getDel_flg() {
        return del_flg;
    }

    public void setDel_flg(int del_flg) {
        this.del_flg = del_flg;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
