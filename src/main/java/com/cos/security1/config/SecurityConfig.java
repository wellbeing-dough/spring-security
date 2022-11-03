package com.cos.security1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity  //스프링 시큐리티 필터가 스프링 필터체인에 등록이 된다
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()    //user 주소로 들어오면 인증, 로그인 한사람 들어올 수 있음
                //manager 쪽으로 들어오면 인증 뿐만 아니라 access 라고 ADMIN 이나 MANAGER 권한이 있는사람만 들어오게
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN'or hasRole('ROLE_MANAGER'))")
                //admin 쪽으로 들어오면 인증 뿐만 아니라 ADMIN 권한이 있어야 들어옴
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                // 다른 모든 요청은 permitAll 그냥 접속하게 해준다 그럼 되나? 안된다
                .anyRequest().permitAll()
                //밑에 세줄을 추가하면 어드민이나 유저나 뭘 들어가도 그냥 접속하게 안하고 로그인 페이지로 가게 해준다
                .and()
                .formLogin()
                .loginPage("/login");
    }
}

