package com.cos.security1.config;

import com.cos.security1.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity  //스프링 시큐리티 필터가 스프링 필터체인에 등록이 된다
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) //secured 어노테이션 활성화, (pre,postAuthorize) 어노테이션 활성화
public class SecurityConfig {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Bean   //Bean 어노테이션 쓰면 해당 메서드의 리턴되는 오브젝트를 ioc 로 등록해준다
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()    //user 주소로 들어오면 인증, 로그인 한사람 들어올 수 있음
                //manager 쪽으로 들어오면 인증 뿐만 아니라 access 라고 ADMIN 이나 MANAGER 권한이 있는사람만 들어오게
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN')or hasRole('ROLE_MANAGER')")
                //admin 쪽으로 들어오면 인증 뿐만 아니라 ADMIN 권한이 있어야 들어옴
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")

                // 다른 모든 요청은 permitAll 그냥 접속하게 해준다 그럼 되나? 안된다
                .anyRequest().permitAll()
                //밑에 세줄을 추가하면 어드민이나 유저나 뭘 들어가도 그냥 접속하게 안하고 로그인 페이지로 가게 해준다
                .and()
                .formLogin()
                .loginPage("/loginForm")
                .loginProcessingUrl("/login")  //login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해준다
                .defaultSuccessUrl("/")    //login 완료되면 메인페이지로
                .and()
                .oauth2Login()
                .loginPage("/loginForm")   //구글 로그인이 완료된 후에 후처리가 필요. 1.코드받기(인증) 2.엑세스토큰받(권한) 3.사용자프로필정보가져
                //와서 그 정보를 토대로 회원가입을 자동으로 진행시키기도 함 그 정보가 모자라면 추가적인 회원가입
                //구글 로그인을하면 엑세스토큰 + 사용자 프로필정보를 한방에 받아온 oauth 라이브러리의 장점
                .userInfoEndpoint()
                .userService(principalOauth2UserService);
        return http.build();
    }
}

