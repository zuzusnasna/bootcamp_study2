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
        //insert
//        memberRepository.save(Member.builder()
//                .name("자비스")
//                .email("zavis@naver.com")
//                .age(23).build());
//
//        //update
//        Member member = Member.builder()
//                .name("원이")
//                .email("onee@naver.com")
//                .age(23).build();
//        memberRepository.save(member);
//
//        var members =  memberRepository.findAll();
//        log.info("{}", members);
//
//        member.setAge(11);
//        memberRepository.save(member);
//        log.info("{}",member);

        //find all members
//        var members = memberRepository.findAll();
//        log.info("{}",members);

        //find member by id
//        var member = memberRepository.findById(1L);
//        log.info("{}",member);

        //findByAgeGreaterThan
        var member = memberRepository.findByAgeGreaterThan(20);
        log.info("{}",member);

//        //삭제
//        memberRepository.deleteById(5L);
//        log.info("회원 삭제 완료 : id = {}", 5L);
    }
}