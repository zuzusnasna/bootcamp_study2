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

@Configuration
@RequiredArgsConstructor
//@RequiredArgsConstructor는 Lombok이 final 필드를 매개변수로 받는 생성자를 자동으로 만들어주는 애너테이션
public class SecurityConfiguration {

    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                authorizationManagerRequestMatcherRegistry.requestMatchers(
                        "/",
                        "/articles/list",
                        "/articles/content"
                ).permitAll()
                        .requestMatchers("/member/**")
                        .hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/signup")
                        .permitAll()
                        .anyRequest().authenticated()
        )
                .httpBasic(Customizer.withDefaults())
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return username -> {
            Member member = memberRepository.findByEmail(username)
                    .orElseThrow();
            return new MemberUserDetails(
                    member,
                    authorityRepository.findByMember(member)
            );
        };
    }
}
