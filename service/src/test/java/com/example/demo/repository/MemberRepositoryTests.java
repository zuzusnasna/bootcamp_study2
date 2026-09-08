package com.example.demo.repository;

import com.example.demo.entity.Member;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * MemberRepository의 기본 동작을 테스트하는 클래스.
 *
 * Repository 테스트에서는 실제 Controller/Service를 거치지 않고
 * Repository가 JPA를 통해 Entity를 정상적으로 저장하고 조회하는지를 확인한다.
 *
 * 테스트 흐름:
 * @BeforeEach → 테스트 데이터 준비
 *      ↓
 * @Test → Repository 동작 검증
 *      ↓
 * @AfterEach → 테스트 데이터 삭제
 */
@SpringBootTest // Spring Boot 애플리케이션 컨텍스트를 로드하여 실제 Repository Bean을 사용한다.
public class MemberRepositoryTests {

    // Spring이 MemberRepository 구현체를 자동으로 주입한다.
    @Autowired
    private MemberRepository memberRepository;

    /**
     * 각 테스트 메서드가 실행되기 전에 테스트 데이터를 저장한다.
     *
     * 테스트마다 동일한 초기 상태를 만들기 위한 준비 단계다.
     */
    @BeforeEach
    void setUp() {
        // 첫 번째 테스트용 회원 저장
        memberRepository.save(Member.builder()
                .name("윤서준")
                .email("Seojun@naver.com")
                .age(14)
                .build());

        // 두 번째 테스트용 회원 저장
        memberRepository.save(Member.builder()
                .name("홍길동")
                .email("Hong@naver.com")
                .age(14)
                .build());
    }

    /**
     * 각 테스트가 끝난 뒤 실행된다.
     *
     * 이전 테스트의 데이터가 다음 테스트에 영향을 주지 않도록
     * Repository에서 모든 회원 데이터를 삭제한다.
     */
    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
    }

    // 필요할 때 특정 테스트를 잠시 비활성화할 수 있는 예시
//    @Disabled("잠시 중단")
    // 같은 테스트를 여러 번 반복 실행할 수 있는 예시
//    @RepeatedTest(3)

    /**
     * Repository의 전체 조회 기능을 테스트한다.
     *
     * setUp()에서 회원 2명을 저장했으므로
     * findAll()의 결과 역시 2명이어야 한다.
     */
    @Test
    void 회원_전체_조회_테스트_() {
        // DB에 저장된 모든 Member Entity를 조회한다.
        List<Member> members = memberRepository.findAll();

        // 조회된 회원 수가 테스트 데이터와 동일한 2명인지 검증한다.
        assertThat(members.size()).isEqualTo(2);
    }
}
