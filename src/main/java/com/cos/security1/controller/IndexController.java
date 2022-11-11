package com.cos.security1.controller;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller //View 를 리턴하겠다!
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test/login")
    public @ResponseBody String testLogin(
            Authentication authentication,
            @AuthenticationPrincipal PrincipalDetails userDetails) {
        System.out.println("/test/login ============");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();//캐스팅 오류
        //출력해보면 둘다 같은 데이터를 가지고 있다
        System.out.println("authentication: " + principalDetails.getUser());
        System.out.println("userDetails: " + userDetails.getUser());

        return "세션 정보 확인하기";
    }

    // 스프링 시큐리티는 자기만의 시큐리티 세션을 들고있다 그 세션에 들어갈 수 있는 타입은 authentication 객체 밖에
    // authentication 을 의존성 주입을 할 수 있다 authentication 안에 들어갈 수 있는 두개의 타입이 있는데
    // 첫번째는 UserDetails 타입 두번째는 OAuth2User 타입이다
    // 정리하면 시큐리티가 들고있는 세션에는 무조건 authentication 객체만 들어갈 수 있고 그게 들어가는 순간 로그인이 된거다
    // authentication 에 들어갈 수잇는 두개의 타입이 들어가는데 일반적인 로그인을 하면 UserDetails 가 들어가고
    // 페북이나 구글 로그인하면 OAuth2User 타입이 들어간다. 우리가 필요할떄 꺼내 써야되는데 불편한게 있다
    // 우리가 일반적인 세션에 접근하려면 UserDetails 로 들어가고 구글로 로그인하려면 OAuth2User 로 들어간다
    // 그럼 컨트롤러에서 일반적인 로그인하려면 둘중 뭘써야될까 정답은 X 라는 클래스를 만들어서 X 에서 UserDetails
    // or OAuth2User 를 implement 로 상속받고 X 를 부모로 두면 된다 근데 이 프로젝트에서 UserDetails 는
    // PrincipalDetails 를 impl 해서 묶었으니까 Authentication 안에 PrincipalDetails 이 들어가는 거다
    // 그러면 OAuth2User 도 PrincipalDetails 타입을 부모로 묶어서 Authentication 객체에 넣으면 우리가 언제
    // 어디서나 필요할 때마다 PrincipalDetails principalDetails 로 받을 수 있다
    // PrincipalDetails 에 UserDetails 랑 OAuth2User 를 implement 하고 오버라이딩해서 구현하면된다

    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOauthLogin(
            Authentication authentication,
            @AuthenticationPrincipal OAuth2User oauth) {
        System.out.println("/test/oauth/login ============");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        //출력해보면 둘다 같은 데이터를 가지고 있다
        System.out.println("authentication: " + oAuth2User.getAttributes());
        System.out.println("oauth2User: " + oauth.getAttributes());
        return "OAuth 세션 정보 확인하기";
    }

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

    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user) {
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user);
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN")  //권한이 admin 만 가능
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")   //이 데이터 메소드가 실행되기 직전에 실행된다 권한 하나주고싶으면
    //Secured 쓰면되는데 이건 두개 동시에가능
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "데이터 정보";
    }
}
