package com.example.demo.service;

import com.example.demo.dto.MemberRequest;
import com.example.demo.dto.MemberResponse;
import com.example.demo.entity.Member;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 회원의 핵심 비즈니스 로직을 담당하는 Service 계층.
 *
 * 전체 흐름:
 * Controller
 *    ↓ Request DTO
 * MemberService
 *    ↓ Entity
 * MemberRepository
 *    ↓
 * Database
 *    ↓ Entity
 * MemberService
 *    ↓ Response DTO
 * Controller -> Client
 *
 * Controller가 DB에 직접 접근하지 않고 Service를 거치게 하여
 * 요청 처리와 비즈니스 로직을 분리한다.
 */
@Service // Spring이 이 클래스를 Service Bean으로 관리
public class MemberService {

    // Repository를 통해 회원 Entity를 DB에 저장/조회/수정/삭제한다.
    @Autowired
    private MemberRepository memberRepository;

    /**
     * 회원 한 명을 생성한다.
     *
     * 처리 순서:
     * 1. MemberRequest에서 입력값을 꺼낸다.
     * 2. Builder를 이용해 Member Entity를 만든다.
     * 3. Repository.save()로 DB에 저장한다.
     * 4. 저장된 Entity를 MemberResponse DTO로 변환한다.
     */
    public MemberResponse create(MemberRequest memberRequest) {
        // Request DTO -> Entity 변환
        var member = Member.builder()
                .name(memberRequest.getName())
                .email(memberRequest.getEmail())
                .age(memberRequest.getAge())
                .enabled(true).build();

        // Entity -> Repository -> JPA -> DB 저장
        // save 이후 IDENTITY 전략을 사용하는 id도 DB에서 생성되어 Entity에 반영된다.
        memberRepository.save(member);

        // Entity를 클라이언트에게 그대로 반환하지 않고 Response DTO로 변환한다.
        return mapToMemberResponse(member);
    }

    /**
     * 전체 회원을 조회한다.
     *
     * Repository에서 Entity 목록을 받은 후
     * 각 Entity를 MemberResponse로 변환하여 반환한다.
     */
    public List<MemberResponse> findAll() {
        return memberRepository.findAll()
                .stream()
                // 각 Member Entity에 동일한 변환 메서드를 적용한다.
                .map(this::mapToMemberResponse)
                .toList();
    }

    /**
     * 여러 회원을 한 번에 생성한다.
     *
     * @Transactional을 통해 이 메서드의 DB 작업을 하나의 트랜잭션으로 묶는다.
     * 처리 도중 문제가 발생하면 트랜잭션의 작업이 롤백될 수 있다.
     */
    @Transactional
    public List<MemberResponse> createBatch(List<MemberRequest> memberRequests) {
        // List의 각 Request DTO를 create()에 전달하여 회원을 생성한다.
        // create()의 결과인 MemberResponse들을 다시 List로 모은다.
        return memberRequests.stream()
                .map(this::create)
                .toList();
    }

    /**
     * Entity -> Response DTO 변환 전용 메서드.
     *
     * DB Entity와 외부에 반환할 DTO의 구조를 분리하기 위해 사용한다.
     * private이므로 Service 내부에서만 사용한다.
     */
    private MemberResponse mapToMemberResponse(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .age(member.getAge())
                .build();
    }

    /**
     * 회원 한 명을 ID로 조회한다.
     *
     * Repository.findById()는 Optional<Member>를 반환한다.
     * 데이터가 있으면 Member를 꺼내고,
     * 없으면 NotFoundException을 발생시켜 404 응답으로 연결한다.
     */
    public MemberResponse findById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        return mapToMemberResponse(member);
    }

    /**
     * 회원 정보를 수정한다.
     *
     * 처리 순서:
     * 1. id로 기존 회원 조회
     * 2. 회원이 없으면 NotFoundException
     * 3. 기존 Entity의 값을 변경
     * 4. Repository.save()로 저장
     * 5. Response DTO로 변환하여 반환
     */
    public MemberResponse update(Long id, MemberRequest memberRequest) {
        Member member = memberRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        // 조회한 기존 Entity의 값을 Request DTO 값으로 변경한다.
        member.setName(memberRequest.getName());
        member.setEmail(memberRequest.getEmail());
        member.setAge(memberRequest.getAge());

        // 변경된 Entity를 Repository에 전달하여 DB에 반영한다.
        memberRepository.save(member);

        return mapToMemberResponse(member);
    }

    /**
     * 회원을 삭제한다.
     *
     * 바로 delete하지 않고 먼저 findById로 존재 여부를 확인한다.
     * 회원이 존재하지 않으면 404 Not Found가 발생한다.
     */
    public void delete(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        // 조회된 Member Entity를 Repository에 전달하여 DB에서 삭제한다.
        memberRepository.delete(member);
    }
}
