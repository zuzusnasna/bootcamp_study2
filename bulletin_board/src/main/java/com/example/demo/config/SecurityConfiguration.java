package com.example.demo.config;

import com.example.demo.repository.AuthorityRepository;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
//@RequiredArgsConstructor는 Lombok이 final 필드를 매개변수로 받는 생성자를 자동으로 만들어주는 애너테이션
public class SecurityConfiguration {

    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->)
        return http.build();
    }
}
