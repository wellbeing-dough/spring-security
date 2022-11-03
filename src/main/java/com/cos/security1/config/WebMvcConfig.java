package com.cos.security1.config;

import org.springframework.boot.web.servlet.view.MustacheViewResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        MustacheViewResolver resolver = new MustacheViewResolver(); //내가 만든 view 에
        resolver.setCharset("UTF-8");                               //인코딩은 유티에프 8이고
        resolver.setContentType("text/html; charset=UTF-8");        //내가 던지는 데이터는 html 이고
        resolver.setPrefix("classpath:/templates/");                 //내 프로젝트 경로
        resolver.setSuffix(".html");                 //suffix .html 로 바꾸면 .html 로 바꿔도 머스태치가 인식

        registry.viewResolver(resolver);            // registry 로 viewResolver 등록
    }
}