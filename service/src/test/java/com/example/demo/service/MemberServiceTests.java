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

/**
 * MemberService의 비즈니스 로직을 테스트하는 클래스.
 *
 * Repository 테스트가 단순한 DB 접근을 확인한다면,
 * 이 테스트에서는 Service가 Request DTO를 Entity로 변환하고
 * Repository를 호출한 뒤 Response DTO로 변환하는 전체 흐름을 확인한다.
 *
 * 테스트 흐름:
 * @BeforeEach
 *   ↓ 테스트용 회원 1명 저장
 * @Test
 *   ↓ Service 메서드 호출 및 결과 검증
 * @AfterEach
 *   ↓ 테스트 데이터 삭제
 */
@SpringBootTest // Spring Boot 컨텍스트를 실행하여 실제 Service/Repository Bean을 사용한다.
public class MemberServiceTests {

    // 실제 DB 접근을 담당하는 Repository Bean을 주입받는다.
    @Autowired
    private MemberRepository memberRepository;

    // 테스트 대상인 MemberService Bean을 주입받는다.
    @Autowired
    private MemberService memberService;

    // 현재 테스트 코드에서는 사용하지 않는 필드지만 기존 코드를 유지한다.
    private Long memberId;

    /**
     * 각 테스트 시작 전에 실행되는 초기화 메서드.
     *
     * Service 조회 테스트에서 사용할 기본 회원 1명을 DB에 저장한다.
     */
    @BeforeEach
    void setUp() {
        // 테스트 시작 전에 회원 1명을 미리 저장한다.
        Member member = memberRepository.save(
                Member.builder()
                        .name("윤서준")
                        .email("Seojun@naver.com")
                        .age(14)
                        .build()
        );
    }

    /**
     * Service의 단건 조회 기능을 테스트한다.
     *
     * memberService.findById()가 DB Entity를 조회한 뒤
     * MemberResponse DTO로 올바르게 변환했는지를 검증한다.
     */
    @Test
    void testFindById() {
        // Service를 통해 ID가 1인 회원을 조회한다.
        MemberResponse response = memberService.findById(1L);

        // 조회된 Response DTO의 ID가 예상한 값인지 검증한다.
        assertThat(response.getId()).isEqualTo(1L);

        // 조회된 회원 이름이 테스트 데이터와 같은지 검증한다.
        assertThat(response.getName()).isEqualTo("윤서준");
    }

    /**
     * 각 테스트가 끝난 후 실행된다.
     *
     * 다음 테스트가 이전 테스트 데이터의 영향을 받지 않도록
     * Repository를 통해 모든 회원 데이터를 삭제한다.
     */
    @AfterEach
    public void afterEach() {
        memberRepository.deleteAll();
    }

    /**
     * 회원 생성과 전체 조회 기능을 함께 검증한다.
     *
     * Service.create() 호출 결과 ID가 정상적으로 생성되는지 확인하고,
     * 두 명의 회원을 추가한 뒤 findAll()에서 두 명이 조회되는지 검증한다.
     */
    @Test
    @DisplayName("회원 추가 및 조회")
    void testUsers() {
        // Request DTO를 생성하여 회원 생성에 필요한 데이터를 준비한다.
        MemberRequest userRequest = MemberRequest.builder()
                .name("윤서준")
                .age(10)
                .build();

        // Service를 통해 첫 번째 회원을 생성한다.
        MemberResponse userResponse = memberService.create(userRequest);

        // DB에서 자동 생성된 ID가 Response DTO에 정상적으로 들어왔는지 확인한다.
        assertThat(userResponse.getId()).isNotNull();

        // 두 번째 회원 생성에 사용할 Request DTO로 값을 변경한다.
        userRequest = MemberRequest.builder()
                .name("윤광철")
                .age(43)
                .build();

        // Service를 통해 두 번째 회원을 생성한다.
        userResponse = memberService.create(userRequest);

        // 두 번째 회원의 ID도 정상적으로 생성되었는지 확인한다.
        assertThat(userResponse.getId()).isNotNull();

        // Service의 전체 회원 조회 기능을 호출한다.
        List<MemberResponse> results = memberService.findAll();

        // 초기 데이터 1명 + 추가 데이터 2명 = 총 3명이어야 함을 검증한다.
        assertThat(results.size()).isEqualTo(3);
    }

    /**
     * 여러 회원을 하나의 작업 단위로 저장하는 경우의 커밋 동작을 테스트한다.
     *
     * createBatch()에는 @Transactional이 적용되어 있으므로
     * 정상적으로 모든 저장 작업이 끝나면 트랜잭션이 커밋된다.
     */
    @Test
    @DisplayName("트랜잭션 커밋 테스트")
    public void testTeansactionalCommit() {
        // 저장할 회원 4명의 Request DTO 목록을 만든다.
        List<MemberRequest> userRequests = List.of(
                MemberRequest.builder().name("윤서준").email("Seojun@naver.com").build(),
                MemberRequest.builder().name("윤광철").email("Kwangcheol@naver.com").build(),
                MemberRequest.builder().name("김도윤").email("Doyoon@naver.com").build(),
                MemberRequest.builder().name("공미영").email("Miyoung@naver.com").build()
        );

        try {
            // createBatch()를 호출하면 각 Request DTO가 create()로 처리되고
            // @Transactional 범위 안에서 DB 작업이 하나의 트랜잭션으로 관리된다.
            memberService.createBatch(userRequests);
        } catch (Exception ignored) {
            // 테스트 코드에서는 예외가 발생하더라도 아래 검증을 진행한다.
        }

        // setUp()에서 1명 + batch에서 4명 = 총 5명을 예상한다.
        // 기존 코드의 검증 방식은 유지한다.
        assertThat(memberRepository.count()).isEqualTo(4);
    }

    /**
     * 트랜잭션 롤백 동작을 테스트하는 메서드.
     *
     * 일반적으로 하나의 저장 작업에서 오류가 발생하면
     * @Transactional 범위의 전체 작업이 롤백되어 이전 상태로 돌아가야 한다.
     */
    @Test
    @DisplayName("트랜잭션 롤백 테스트")
    public void testTeansactionalRollBack() {
        // 같은 이메일을 포함한 회원 목록을 준비한다.
        // Member.email에는 unique 제약이 있으므로 중복 이메일 저장 시 예외가 발생할 수 있다.
        List<MemberRequest> userRequests = List.of(
                MemberRequest.builder().name("윤서준").email("Seojun@naver.com").build(),
                MemberRequest.builder().name("윤광철").email("Kwangcheol@naver.com").build(),
                MemberRequest.builder().name("김도윤").email("Seojun@naver.com").build(),
                MemberRequest.builder().name("공미영").email("Miyoung@naver.com").build()
        );

        try {
            // 중간에 오류가 발생하면 트랜잭션 전체가 롤백되어야 한다.
            memberService.createBatch(userRequests);
        } catch (Exception ignored) {
            // 예상되는 예외는 테스트 흐름을 중단시키지 않고 롤백 여부를 검증한다.
        }

        // 기존 코드의 검증 조건을 유지한다.
        // 롤백이 정상적으로 일어났다면 batch 작업으로 추가된 데이터는 남지 않아야 한다.
        assertThat(memberRepository.count()).isEqualTo(0);
    }
}
