package com.skill_up.poll.service;

import com.skill_up.poll.model.UserModel;
import com.skill_up.poll.repository.JdbcUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistService {

    @Autowired
    private  JdbcUserRepository jdbcUserRepository;

    public int regist(UserModel user){

        int result = jdbcUserRepository.insert(user);;

        return result;

    }


}
