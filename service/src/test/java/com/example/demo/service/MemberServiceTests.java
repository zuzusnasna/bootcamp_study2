package com.example.demo.service;

import com.example.demo.dto.MemberRequest;
import com.example.demo.dto.MemberResponse;
import com.example.demo.entity.Member;
import com.example.demo.repository.MemberRepository;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class MemberServiceTests {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;
    private Long memberId;

    @BeforeEach
    void setUp(){
        Member member = memberRepository.save(
                Member.builder()
                        .name("윤서준")
                        .email("Seojun@naver.com")
                        .age(14)
                        .build()
        );
    }

    @Test
    void testFindById(){
        MemberResponse response = memberService.findById(1L);
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("윤서준");
    }

    @AfterEach
    public void afterEach(){
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 추가 및 조회")
    void testUsers(){
        //윤서준 회원을 추가하고 아이디가 자동으로 생성되었는지를 검증한다.
        MemberRequest userRequest = MemberRequest.builder().name("윤서준").age(10).build();
        MemberResponse userResponse = memberService.create(userRequest);
        assertThat(userResponse.getId()).isNotNull();

        //윤광철 회원을 추가하고 아이디가 자동으로 생성 되었는지를 검증한다.
        userRequest = MemberRequest.builder().name("윤광철").age(43).build();
        userResponse = memberService.create(userRequest);
        assertThat(userResponse.getId()).isNotNull();

        //회원을 모두 조회해 두 명이 조회되는지를 검증한다.
        List<MemberResponse> results = memberService.findAll();
        assertThat(results.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("트랜젝션 커밋 테스트")
    public void testTeansactionalCommit(){
        //모두 네명의 회원을 추가하며 데이터에 오류가 없기 때문에
        //트렌잭션에서 4개의 입력 모두 커밋되어야 한다.
        List<MemberRequest> userRequests = List.of(
                MemberRequest.builder().name("윤서준").email("Seojun@naver.com").build(),
                MemberRequest.builder().name("윤광철").email("Kwangcheol@naver.com").build(),
                MemberRequest.builder().name("김도윤").email("Doyoon@naver.com").build(),
                MemberRequest.builder().name("공미영").email("Miyoung@naver.com").build()
        );
        try {
            memberService.createBatch(userRequests);
        }catch (Exception ignored){}
        assertThat(memberRepository.count()).isEqualTo(4);
    }

    @Test
    @DisplayName("트랜젝션 롤백 테스트")
    public void testTeansactionalRollBack(){
        //모두 네명의 회원을 추가하며 데이터에 오류가 없기 때문에
        //트렌잭션에서 4개의 입력 모두 커밋되어야 한다.
        List<MemberRequest> userRequests = List.of(
                MemberRequest.builder().name("윤서준").email("Seojun@naver.com").build(),
                MemberRequest.builder().name("윤광철").email("Kwangcheol@naver.com").build(),
                MemberRequest.builder().name("김도윤").email("Seojun@naver.com").build(),
                MemberRequest.builder().name("공미영").email("Miyoung@naver.com").build()
        );
        try {
            memberService.createBatch(userRequests);
        }catch (Exception ignored){}
        assertThat(memberRepository.count()).isEqualTo(0);
    }


}
