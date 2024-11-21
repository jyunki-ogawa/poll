package com.skill_up.poll.repository;

import com.skill_up.poll.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public class JdbcUserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcUserRepository(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;

    }

    @Autowired
    PasswordEncoder passwordEncoder;

    public int insert(UserModel user) {

        int result = jdbcTemplate.update("INSERT INTO users(password, nickname,username, role) values ( ?, ?, ?, ?)",
                passwordEncoder.encode(user.getPassword()),
                user.getNickname(),
                user.getUsername(),
                user.getRole()
            );

        return  result;
    }

    //UserDetailsServiceのloadUserByUsernameで仕様されることを想定
    public UserModel findByUsername(String username){

        UserModel user = jdbcTemplate.queryForObject("SELECT * FROM users WHERE username=?", new DataClassRowMapper<>(UserModel.class), username);
        return user;

    }

}
