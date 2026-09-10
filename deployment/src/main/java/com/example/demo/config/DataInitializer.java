package com.example.demo.config;

import com.example.demo.model.Member;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Profile("dev") //dev 환경에서만 실행하게해주는 애노테이션
public class DataInitializer implements ApplicationRunner {

    private final MemberRepository memberRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        memberRepository.save(Member.builder()
                .name("윤서준")
                .email("Seojun@naver.com")
                .age(10).build());
        memberRepository.save(Member.builder()
                .name("윤광철")
                .email("Kwangcheol@naver.com")
                .age(23).build());
    }
}
