package com.example.demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JdbcApplication implements ApplicationRunner {
    private final MemberRepository memberRepository;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        memberRepository.save(Member.builder()
                .name("자비스")
                .email("javis@naver.com")
                .age(23).build());

        memberRepository.save(Member.builder()
                .name("원이")
                .email("one2@naver.com")
                .age(21).build());

        var members =  memberRepository.findAll();
        log.info("{}", members);
    }
}