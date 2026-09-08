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
 * Member Entity는 회원 한 명의 데이터를 표현한다.
 * Article과 일대다(OneToMany) 관계를 가지고 있어
 * 하나의 회원이 여러 게시글을 작성할 수 있다.
 *
 * 전체 흐름:
 * MemberRequest
 *      ↓
 * MemberService
 *      ↓
 * Member Entity
 *      ↓
 * JPA / Hibernate
 *      ↓
 * Database
 *      ↓
 * Member Entity
 *      ↓
 * MemberResponse
 */
@Data // getter/setter 등 반복적인 메서드를 Lombok이 자동 생성한다.
@Entity // 이 클래스를 JPA가 관리하는 Entity로 등록한다.
@Builder // Builder 패턴으로 Member 객체를 생성할 수 있게 한다.
@AllArgsConstructor // 모든 필드를 받는 생성자를 생성한다.
@NoArgsConstructor // JPA가 Entity를 생성할 때 필요한 기본 생성자를 생성한다.
public class Member {

    /**
     * 회원의 기본 키(PK).
     *
     * IDENTITY 전략을 사용하므로 INSERT 시 DB가 ID 값을 생성한다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 회원 이름
    private String name;

    /**
     * 회원 이메일.
     *
     * unique = true를 지정하면 DB에서 동일한 이메일의 중복 저장을 막는다.
     * 따라서 이메일은 회원을 구분하는 고유한 값으로 사용된다.
     */
    @Column(unique = true) // 이메일 중복 허용 X
    private String email;

    // 회원 나이
    private Integer age;

    /**
     * 회원 비밀번호.
     *
     * 현재 MemberRequest / MemberResponse DTO에서는 사용하지 않기 때문에
     * API 응답에 직접 노출되지 않는다.
     */
    private String password;

    // 계정 활성화 여부. true이면 활성화된 계정으로 사용할 수 있다.
    private boolean enabled;

    /**
     * 회원이 작성한 게시글 목록.
     *
     * mappedBy = "member"
     * → 관계의 주인은 Article.member 필드임을 의미한다.
     * 즉, Member 테이블이 외래 키를 관리하는 것이 아니라
     * Article 테이블의 member_id가 실제 관계를 관리한다.
     *
     * cascade = CascadeType.ALL
     * → Member에 대한 영속성 작업을 Article에도 전파한다.
     *
     * orphanRemoval = true
     * → Member와의 관계에서 고아가 된 Article을 삭제할 수 있도록 한다.
     *
     * 현재 구조에서는 회원 삭제 시 연결된 게시글도 함께 삭제되도록 설정되어 있다.
     */
    @OneToMany(
            mappedBy = "member",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Article> articles;
}
