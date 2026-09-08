package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 회원 정보를 DB 테이블과 연결하는 JPA Entity.
 *
 * 흐름:
 * MemberRequest -> Service -> Member Entity -> JPA/Hibernate -> DB
 *                                      ↓
 *                              MemberResponse
 *
 * Entity는 실제 DB에 저장되는 데이터 구조를 표현한다.
 */
@Data // getter/setter 등 반복적인 메서드를 Lombok이 생성
@Entity // 이 클래스를 JPA가 관리하는 Entity로 등록
@Builder // 객체를 builder 방식으로 생성할 수 있게 한다.
@AllArgsConstructor // 모든 필드를 받는 생성자 생성
@NoArgsConstructor // JPA가 Entity를 생성할 때 필요한 기본 생성자
public class Member {

    /**
     * 회원의 기본 키(PK).
     * IDENTITY 전략에서는 DB가 INSERT 시 ID를 생성한다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 회원 이름
    private String name;

    /**
     * 이메일 컬럼.
     * unique = true이므로 DB에서 동일한 이메일의 중복 저장을 막는다.
     */
    @Column(unique = true) // 이메일 중복 허용 X
    private String email;

    // 회원 나이
    private Integer age;

    // 회원 비밀번호. 현재 Request/Response DTO에는 포함하지 않는다.
    private String password;

    // 계정 활성화 여부. true이면 활성화된 계정으로 사용할 수 있다.
    private boolean enabled;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    //회원 삭제시 게시글도 자동 삭제
    private List<Article> articles;
}
