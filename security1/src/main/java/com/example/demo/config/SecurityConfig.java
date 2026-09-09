package com.example.demo.config;

import com.example.demo.repository.MemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class SecurityConfig {

    // 비밀번호는 평문으로 저장하지 않고 BCrypt 해시값으로 저장·검증한다.
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // DB에서 회원 정보를 조회하여 Spring Security가 이해할 수 있는 UserDetails로 변환한다.
    @Bean
    public UserDetailsService userDetailsService(MemberRepository memberRepository){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                // username이라는 매개변수명과 실제 로그인 식별자는 별개이며,
                // 이 실습에서는 이메일을 로그인 ID로 사용하므로 findByEmail()로 조회한다.
                var member = memberRepository.findByEmail(username).orElseThrow(new Supplier<UsernameNotFoundException>() {

                    @Override
                    public UsernameNotFoundException get() {
                        return new UsernameNotFoundException("User not founded" + username);
                    }
                });

                // DB에 저장된 BCrypt 해시 비밀번호와 회원의 권한을 Spring Security에 전달한다.
                return User.builder()
                        .username(username)
                        .password(member.getPassword())
                        .authorities(member.getAuthority()).build();
            }
        };
    }

    // URL별 접근 권한을 설정하는 SecurityFilterChain을 Bean으로 등록해야 Spring Security가 적용된다.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http.authorizeHttpRequests(new Customizer<AuthorizeHttpRequestsConfigurer<org.springframework.security.config.annotation.web.builders.HttpSecurity>.AuthorizationManagerRequestMatcherRegistry>() {
            @Override
            public void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize) {
                authorize.requestMatchers("/", "/home").permitAll() // 모든 사용자 접근 가능
                        .requestMatchers("/member/**").hasAnyAuthority("ROLE_ADMIN") // ADMIN 권한 사용자만 접근 가능
                        .anyRequest().authenticated(); // 그 외 요청은 로그인한 사용자만 접근 가능
            }
        }).formLogin(Customizer.withDefaults()) // 기본 로그인 페이지 사용
                .logout(Customizer.withDefaults()); // 기본 로그아웃 처리 사용
        return http.build();
    }
}
