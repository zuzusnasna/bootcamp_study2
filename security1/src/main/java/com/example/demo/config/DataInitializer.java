package com.example.demo.config;

import com.example.demo.model.Member;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DataInitializer implements ApplicationRunner {

    private final MemberRepository memberRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 애플리케이션 실행 시 Spring Security 테스트용 회원 데이터를 생성한다.
        var passwordEncoder = new BCryptPasswordEncoder();

        // 일반 사용자: 로그인은 가능하지만 /member/** 접근 권한은 없다.
        memberRepository.save(Member.builder()
                .name("윤서준")
                .email("Seojun@naver.com")
                .age(10)
                .password(passwordEncoder.encode("password")) // 평문 대신 BCrypt 해시 저장
                .authority("ROLE_USER")
                .build());

        // 관리자: ROLE_ADMIN 권한으로 /member/** 접근이 가능하다.
        memberRepository.save(Member.builder()
                .name("윤광철")
                .email("Kwangcheol@naver.com")
                .age(32)
                .password(passwordEncoder.encode("password"))
                .authority("ROLE_ADMIN")
                .build());
    }
}
