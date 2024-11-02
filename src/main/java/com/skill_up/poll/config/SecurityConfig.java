package com.skill_up.poll.config;

import com.skill_up.poll.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService users() {
        return new UserService();
    }

    @Bean
    //引数で受け取っているのは、SecurityFilterChainオブジェクトを生成してくれるHttpSecurityオブジェクト
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{

        //HttpSecurityオブジェクトのauthorizeHttpRequestsメソッドで、取得した戻り値に対してrequestMatchersメソッドでパスやHTTPメソッドを指定
        //「/admine」を含んだパスで、「ADMIN」という権限をユーザが持っている場合だけアクセスを許可
        http

            .authorizeHttpRequests()
//            .requestMatchers(HttpMethod.POST,"/admin/**").hasRole("USER")
            //POST以外の「/admin/**」へのリクエストに対する設定
            .requestMatchers("/topic/archive").hasAnyRole("ADMIN","USER")
            .requestMatchers("/topic/edit").hasAnyRole("ADMIN","USER")
            .requestMatchers("/topic/create").hasAnyRole("ADMIN","USER")
            //全てのリクエストに対する設定
            .anyRequest().permitAll()
        .and()
            //未認証のユーザからのリクエストが飛んできたら、自動的にGETメソッドで「/login」に遷移させる
            .formLogin()
            //デフォルトでは、ログイン画面を表示する際のパス（ここでは「/login」）に対して、POSTメソッドでリクエストを送信すると、SpringSecurityはログインのリクエストと認識し、認証の処理を実行する
            .loginPage("/login")
            .defaultSuccessUrl("/")
            .failureUrl("/login?failure")
        .and()
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
//        .and()
//            .exceptionHandling()
//            .accessDeniedPage("/")
	    ;

        return http.build();

    }

}
