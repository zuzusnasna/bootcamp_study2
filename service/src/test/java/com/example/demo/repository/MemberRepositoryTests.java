package com.example.demo.repository;

import com.example.demo.entity.Member;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp(){
        memberRepository.save(Member.builder()
                .name("윤서준")
                .email("Seojun@naver.com")
                .age(14)
                .build());

        memberRepository.save(Member.builder()
                .name("홍길동")
                .email("Hong@naver.com")
                .age(14)
                .build());
    }

    @AfterEach
    void tearDown(){
        memberRepository.deleteAll();
    }

//    @Disabled("잠시 중단")
//    @RepeatedTest(3)
    @Test
    void 회원_전체_조회_테스트_(){
        List<Member> members = memberRepository.findAll();
        assertThat(members.size()).isEqualTo(2);
    }
}
