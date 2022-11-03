package com.cos.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller //View 를 리턴하겠다!
public class IndexController {
    //localhost:8080/
    //localhostL8080
    @GetMapping({"","/"})
    public String index() {
        //머스테치 사용해 볼거다 기본폴더 src/main/resources/
        //view resolver 설정: template (prefix),.mustache (suffix) -> application.yml 에 있음
        return "index"; //View 가 된다
    }

    @GetMapping("/user")
    public @ResponseBody String user() {
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    @GetMapping("/login")
    public @ResponseBody String login() {
        return "login";
    }

    @GetMapping("/join")
    public @ResponseBody String join() {
        return "join";
    }
    @GetMapping("/joinProc")
    public @ResponseBody String joinProc() {
        return "회원가입이 만료됨!";
    }

}
