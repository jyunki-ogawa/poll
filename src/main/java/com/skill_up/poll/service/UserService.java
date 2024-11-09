package com.skill_up.poll.service;

import com.skill_up.poll.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import com.skill_up.poll.repository.JdbcUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserService implements UserDetailsService {

    @Autowired
    public JdbcUserRepository repository;

    @Override    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //リポジトリのところのメソッドは作る必要がある。（これはJPAでの実装）
        UserModel user = repository.findByUsername(username);
        return user;

    }
}
