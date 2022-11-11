package com.cos.security1.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    // 구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 서버의 기본정보 ex) 클리언트 아이디 oauth 아이디, secret, scope
        //registrationId fh djEJs OAuth 로 로그인 했는지 확인 가능
        System.out.println("getClientRegistration: " + userRequest.getClientRegistration());
        //엑세스 토큰 -> 구글이 로그인이 완료된 후 후처리가 필요한데 코드가 필요한게 아니라 코드를 통해서 엑세스 토큰을 요청받아서 엑세스 토큰으로 사용자 프로필
        //정보가 리턴
        System.out.println("getAccessToken: " + userRequest.getAccessToken().getTokenValue());
        System.out.println("getAttributes: " + super.loadUser(userRequest).getAttributes());
        System.out.println("getAuthorities: " + super.loadUser(userRequest).getAuthorities());
        //구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인 완료 -> code 리턴(OAuth-Client 라이브러리) -> 코드 통해 AccessToken 요청
        // -> userRequest 정보 -> 회원 프로필 받아야함(loadUser 함수) -> 회원 프로필 -> 구글로부터 회원 프로필 받아준다
        OAuth2User oAuth2User = super.loadUser(userRequest);

        return super.loadUser(userRequest);
    }
}
