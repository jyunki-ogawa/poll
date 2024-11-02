package com.skill_up.poll.model;

import com.skill_up.poll.service.RegistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;

@Component
public class AbstractModel<T> {

    private HttpSession session;

    protected AbstractModel(HttpSession session){
        this.session = session;
    }

    public <T> void setSession(String sessionName, T val){

        if(sessionName.isEmpty()) {
            throw new IllegalArgumentException("SESSION_NAMEを指定してください");
        }

        session.setAttribute(sessionName, val);

    }

    public <T> T getSession(String sessionName) {

        return (T)session.getAttribute(sessionName);

    }

    public void clearSession(String sessionName) {

        setSession(sessionName,null);

    }

    public <T> T getSessionAndFlush(String sessionName) {

        try {

            return getSession(sessionName);

        } finally {

            clearSession(sessionName);

        }

    }


}
