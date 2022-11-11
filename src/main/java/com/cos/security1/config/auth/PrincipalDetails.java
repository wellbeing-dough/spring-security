package com.cos.security1.config.auth;

// 시큐리티가 /login 주소요청이 오면 낚아채서 로그인을 진행시킨다.
// 로그인 진행이 완료가 되면 시큐리티 세션을 만들어 준다. (Security ContextHolder) 라는 키값에 세션정보 저장
// 세션에 들어갈 수 있는 정보는 시큐리티가 가지고있는 세션에 들어갈수잇는 오브젝트가 정해져 있음
// 오브젝트 -> Authentication 타입 객체
// Authentication 안에 User 정보가 있어야 됨
// User 오브젝트 타입 -> UserDetails 타입 객체
// 시큐리티가 가지고있는 세션 영역 = Security Session (여기에 세션 저장)
// Security Session 에 드갈수잇는 객체는 Authentication
// Authentication 객체 안에 User 정보를 저장할때 타입은 UserDetails 이다

// 결론은 Security Session 에 있는 세션 정보를 get 으로 꺼내면
// Authentication 객체가 나오고 이안에서 UserDetails 객체를 꺼내면
// User 오브잭트에 접근을 할 수 있다
// UserDetails 를 implement 해줘야 한다

import com.cos.security1.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Getter
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user;  //콤포지션

    public PrincipalDetails(User user) {
        this.user = user;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    // 해당 유저의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    //니 계정 만료되지 않았니?
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //니 게정 잠기지 않았니?
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //너 비밀번호 오래된거 아니니?
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //니 계정 활성화되지 않았니?
    @Override
    public boolean isEnabled() {
        // 우리 사이트 1년동안 로그인을 안하면 휴먼 계정으로 하기로 함.
        // 현재 시간 - 로그인 시간(User.class 에 멤버 추가) = 1년을 초과하면 return false
        return true;
    }

    @Override
    public String getName() {
        return null;
    }
}
