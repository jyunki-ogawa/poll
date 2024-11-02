package com.skill_up.poll.libs;
import com.skill_up.poll.model.AbstractModel;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.HashMap;

//セッションの格納の形式を指定するために、AbstractModelのジェネリクスに型を指定する
public class Msg extends AbstractModel<HashMap<String,ArrayList<String>>> {

    private HttpSession httpSession;

    public static String SESSION_NAME = "_msg";
    public static final String ERROR = "error";
    public static final String INFO = "info";
    public static final String DEBUG = "debug";

    public Msg(HttpSession session){
        super(session);
    }

    public void push(String type, String msg) {
        if (getSession(SESSION_NAME) == null){
            init();
        }

        HashMap<String,ArrayList<String>> msgsWithTypeList = getSession(SESSION_NAME);
        ArrayList<String> msgs = msgsWithTypeList.get(type);
        msgs.add(msg);
        msgsWithTypeList.put(type, msgs);
        setSession(SESSION_NAME, msgsWithTypeList);
    }

    private void init() {
        HashMap<String,ArrayList<String>> val = new HashMap<>();
        val.put(this.ERROR, new ArrayList<String>());
        val.put(this.INFO, new ArrayList<String>());
        val.put(this.DEBUG, new ArrayList<String>());
        setSession(SESSION_NAME, val);
    }
}
