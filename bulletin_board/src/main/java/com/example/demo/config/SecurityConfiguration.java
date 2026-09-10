package com.example.demo.config;

import com.example.demo.model.Member;
import com.example.demo.model.MemberUserDetails;
import com.example.demo.repository.AuthorityRepository;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// Spring Security의 전체 보안 설정을 담당하는 Configuration 클래스
@Configuration
@RequiredArgsConstructor
// @RequiredArgsConstructor는 final 필드를 매개변수로 받는 생성자를 Lombok이 자동으로 만들어줍니다.
public class SecurityConfiguration {

    // 회원 정보를 조회하기 위한 Repository
    private final MemberRepository memberRepository;

    // 회원의 권한 정보를 조회하기 위한 Repository
    private final AuthorityRepository authorityRepository;

    // Spring Security의 인증/인가 및 로그인/로그아웃 방식을 설정하는 Bean
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                authorizationManagerRequestMatcherRegistry.requestMatchers(
                        // 로그인하지 않아도 누구나 접근할 수 있는 URL
                        "/",
                        "/article/list",
                        "/article/content",
                        "/image/**"
                ).permitAll()
                        // /member/** 경로는 ROLE_ADMIN 권한을 가진 사용자만 접근 가능
                        .requestMatchers("/member/**")
                        .hasAuthority("ROLE_ADMIN")
                        // 회원가입 페이지는 로그인하지 않아도 접근 가능
                        .requestMatchers("/signup")
                        .permitAll()
                        // 위에서 허용하지 않은 나머지 요청은 인증된 사용자만 접근 가능
                        .anyRequest().authenticated()
        )
                // HTTP Basic 인증 방식을 기본 설정으로 활성화
                .httpBasic(Customizer.withDefaults())
                // 폼 로그인 설정
                .formLogin(form -> form
                        // 직접 만든 로그인 페이지의 URL
                        .loginPage("/login")
                        // 로그인 성공 후 이동할 기본 URL
                        .defaultSuccessUrl("/", true)
                        // 로그인 페이지 자체는 인증 없이 접근 가능
                        .permitAll()
                )
                // 로그아웃 설정
                .logout(logout -> logout
                        // 로그아웃 요청 URL
                        .logoutUrl("/logout")
                        // 로그아웃 성공 후 로그인 화면으로 이동
                        .logoutSuccessUrl("/login")
                        // 로그아웃 시 세션을 무효화
                        .invalidateHttpSession(true)
                        // 인증 정보를 삭제
                        .clearAuthentication(true)
                        // 로그아웃 기능은 인증 여부와 관계없이 접근 가능
                        .permitAll()
                );

        // 지금까지 설정한 SecurityFilterChain을 생성해서 Spring에 반환
        return http.build();
    }

    // 비밀번호를 안전하게 암호화하고 비교하기 위한 PasswordEncoder Bean
    @Bean
    public PasswordEncoder passwordEncoder(){
        // BCrypt 방식으로 비밀번호를 암호화
        return new BCryptPasswordEncoder();
    }

    // Spring Security가 로그인한 사용자의 정보를 조회할 때 사용하는 서비스
    @Bean
    public UserDetailsService userDetailsService(){
        // 로그인할 때 전달받은 username을 이용해 회원을 조회
        return username -> {
            // 이 프로젝트에서는 회원의 email을 로그인 아이디(username)로 사용
            Member member = memberRepository.findByEmail(username)
                    // 회원을 찾지 못하면 예외 발생
                    .orElseThrow();

            // DB의 Member와 Authority 정보를 Spring Security가 이해할 수 있는
            // UserDetails 구현체인 MemberUserDetails로 변환
            return new MemberUserDetails(
                    member,
                    authorityRepository.findByMember(member)
            );
        };
    }
}
