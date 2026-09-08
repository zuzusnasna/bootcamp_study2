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
 * Repository 테스트가 DB 접근 자체를 확인한다면,
 * 이 테스트에서는 Service를 호출했을 때 Request DTO → Entity → Repository → Response DTO로
 * 이어지는 비즈니스 로직이 의도대로 동작하는지를 확인한다.
 *
 * 기본 테스트 흐름:
 * @BeforeEach → 테스트 데이터 준비
 *       ↓
 * @Test → Service 기능 실행 및 결과 검증
 *       ↓
 * @AfterEach → 테스트 데이터 정리
 */
@SpringBootTest // Spring Boot 컨텍스트를 실행하고 필요한 Service/Repository Bean을 주입받는다.
public class MemberServiceTests {

    // 회원 Entity의 저장/조회/삭제를 담당하는 Repository Bean.
    @Autowired
    private MemberRepository memberRepository;

    // 실제 테스트 대상인 MemberService Bean.
    @Autowired
    private MemberService memberService;

    // 기존 테스트 코드에 선언되어 있던 필드이므로 유지한다.
    private Long memberId;

    /**
     * 각 테스트가 시작되기 전에 실행되는 준비 단계.
     *
     * 테스트에서 조회할 수 있도록 회원 한 명을 미리 DB에 저장한다.
     */
    @BeforeEach
    void setUp() {
        // Repository를 직접 호출하여 테스트용 회원 1명을 저장한다.
        Member member = memberRepository.save(
                Member.builder()
                        .name("윤서준")
                        .email("Seojun@naver.com")
                        .age(14)
                        .build()
        );
    }

    /**
     * MemberService의 단건 조회 기능을 테스트한다.
     *
     * Service가 Repository에서 회원을 조회하고
     * Member Entity를 MemberResponse DTO로 올바르게 변환하는지 확인한다.
     */
    @Test
    void testFindById() {
        // Service를 호출하여 ID가 1인 회원을 조회한다.
        MemberResponse response = memberService.findById(1L);

        // 조회된 DTO의 ID가 기대한 값인지 검증한다.
        assertThat(response.getId()).isEqualTo(1L);

        // 조회된 회원 이름이 테스트 데이터와 동일한지 검증한다.
        assertThat(response.getName()).isEqualTo("윤서준");
    }

    /**
     * 각 테스트가 끝난 후 실행되는 정리 단계.
     *
     * 한 테스트에서 사용한 데이터가 다음 테스트에 영향을 주지 않도록
     * Repository의 모든 데이터를 삭제한다.
     */
    @AfterEach
    public void afterEach() {
        memberRepository.deleteAll();
    }

    /**
     * 회원 생성과 전체 조회를 테스트한다.
     *
     * Service.create()의 결과로 ID가 자동 생성되는지 확인하고,
     * 생성된 회원을 findAll()로 조회할 수 있는지 검증한다.
     */
    @Test
    @DisplayName("회원 추가 및 조회")
    void testUsers() {
        // 회원 생성 요청에 사용할 Request DTO를 만든다.
        MemberRequest userRequest = MemberRequest.builder()
                .name("윤서준")
                .age(10)
                .build();

        // Request DTO를 Service에 전달하여 회원을 생성한다.
        MemberResponse userResponse = memberService.create(userRequest);

        // DB 저장 과정에서 ID가 정상적으로 생성되었는지 검증한다.
        assertThat(userResponse.getId()).isNotNull();

        // 두 번째 회원 생성용 Request DTO를 만든다.
        userRequest = MemberRequest.builder()
                .name("윤광철")
                .age(43)
                .build();

        // Service를 통해 두 번째 회원을 생성한다.
        userResponse = memberService.create(userRequest);

        // 두 번째 회원도 ID가 정상적으로 생성되었는지 확인한다.
        assertThat(userResponse.getId()).isNotNull();

        // Service의 전체 회원 조회 기능을 호출한다.
        List<MemberResponse> results = memberService.findAll();

        // 기존 코드의 검증 조건을 그대로 유지한다.
        assertThat(results.size()).isEqualTo(2);
    }

    /**
     * 여러 회원을 하나의 트랜잭션으로 처리하는 커밋 테스트.
     *
     * MemberService.createBatch()에는 @Transactional이 적용되어 있으므로
     * 정상적인 저장이 완료되면 하나의 트랜잭션으로 커밋된다.
     */
    @Test
    @DisplayName("트랜잭션 커밋 테스트")
    public void testTeansactionalCommit() {
        // 한 번에 저장할 회원들의 Request DTO를 List로 만든다.
        List<MemberRequest> userRequests = List.of(
                MemberRequest.builder().name("윤서준").email("Seojun@naver.com").build(),
                MemberRequest.builder().name("윤광철").email("Kwangcheol@naver.com").build(),
                MemberRequest.builder().name("김도윤").email("Doyoon@naver.com").build(),
                MemberRequest.builder().name("공미영").email("Miyoung@naver.com").build()
        );

        try {
            // createBatch()가 List의 각 Request DTO를 처리한다.
            // @Transactional 범위 안에서 전체 작업이 하나의 트랜잭션으로 관리된다.
            memberService.createBatch(userRequests);
        } catch (Exception ignored) {
            // 기존 테스트 코드와 동일하게 예외를 무시하고 아래 검증을 수행한다.
        }

        // 기존 코드의 검증 조건을 그대로 유지한다.
        assertThat(memberRepository.count()).isEqualTo(4);
    }

    /**
     * 트랜잭션 롤백 동작을 테스트한다.
     *
     * 여러 작업을 하나의 트랜잭션으로 묶었을 때 중간에 예외가 발생하면
     * 지금까지 처리된 작업까지 함께 취소되어야 한다.
     */
    @Test
    @DisplayName("트랜잭션 롤백 테스트")
    public void testTeansactionalRollBack() {
        // 세 번째 회원이 첫 번째 회원과 같은 이메일을 가지고 있다.
        // Member Entity의 email 컬럼에는 unique 제약이 있으므로 저장 과정에서 예외가 발생할 수 있다.
        List<MemberRequest> userRequests = List.of(
                MemberRequest.builder().name("윤서준").email("Seojun@naver.com").build(),
                MemberRequest.builder().name("윤광철").email("Kwangcheol@naver.com").build(),
                MemberRequest.builder().name("김도윤").email("Seojun@naver.com").build(),
                MemberRequest.builder().name("공미영").email("Miyoung@naver.com").build()
        );

        try {
            // batch 작업 도중 예외가 발생하면 @Transactional에 의해 전체 작업이 롤백되어야 한다.
            memberService.createBatch(userRequests);
        } catch (Exception ignored) {
            // 예외 자체보다 롤백 결과를 확인하기 위해 예외를 잡는다.
        }

        // 기존 코드의 검증 조건을 그대로 유지한다.
        // 롤백이 정상적으로 수행되었는지를 count 결과로 확인한다.
        assertThat(memberRepository.count()).isEqualTo(0);
    }
}
