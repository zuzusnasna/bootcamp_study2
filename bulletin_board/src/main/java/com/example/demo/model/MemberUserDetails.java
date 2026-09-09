package com.example.demo.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

//MemberUserDetails는 회원가입용 객체도 아니고 DB 저장용 Entity도 아님.
//단지 Member를 Spring Security가 사용할 수 있는 형태로 변환해주는 객체
@Data
public class MemberUserDetails implements UserDetails { //Member를 Spring Security가 사용할 수 있는 형태로 변환해주는 객체

    private String username;
    private String password;
    private List<? extends GrantedAuthority> authorities;

    private String displayName;
    private Long memberId;

    @Builder
    public MemberUserDetails(Member member, List<Authority> authorities) {
//        this.username = member.getEmail();
//        Member
//                email = hong@example.com
//                           ↓
//        MemberUserDetails
//                username = hong@example.com
        this.username = member.getEmail();
        this.displayName = member.getName();
        this.password = member.getPassword();
        this.memberId = member.getId();
        this.authorities = authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .toList();
//        Spring Security는 Authority를 그대로 못 씀
//       Authority는 만든 JPA 객체야.

//           Authority
//              ├─ id
//              ├─ authority
//              └─ member

//      Spring Security가 원하는 권한 타입은
//        GrantedAuthority 이므로 변환이 필요.

//        우리 프로그램의 Authority
//          ↓
//        Spring Security의 GrantedAuthority

//        new SimpleGrantedAuthority(authority.getAuthority())
//        authority.getAuthority() 의 결과가 예를 들어 ROLE_ADMIN이면
//        new SimpleGrantedAuthority("ROLE_ADMIN")이 만들어짐
//
//        Authority
//                authority = "ROLE_ADMIN"
//                              ↓ 변환
//        SimpleGrantedAuthority
//                authority = "ROLE_ADMIN"

        //authorities.stream() -> List에 들어있는 여러개의 Authority들을 한개씩 처리하겠다는 말 . stream()


//        .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
        //->각 Authority를 SimpleGrantedAuthority로 바꿔라
    }
}

