package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.h2.api.DatabaseEventListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/**
 * 게시글 정보를 DB 테이블과 연결하는 JPA Entity.
 *
 * Article은 하나의 Member가 여러 개의 게시글을 작성할 수 있는 관계를 가진다.
 * 따라서 관계는 다음과 같다.
 *
 * Article N : 1 Member
 *
 * 또한 EntityListeners를 통해 생성일/수정일을 JPA Auditing으로 관리한다.
 */
@Entity // 이 클래스를 JPA가 관리하는 Entity로 등록한다.
@Data // getter/setter 등 반복적인 메서드를 Lombok이 생성한다.
@Builder // Builder 패턴으로 Article 객체를 생성할 수 있게 한다.
@AllArgsConstructor // 모든 필드를 받는 생성자를 생성한다.
@NoArgsConstructor // JPA가 Entity를 생성할 때 필요한 기본 생성자를 생성한다.
@EntityListeners(AuditingEntityListener.class) // @CreatedDate/@LastModifiedDate를 자동 처리한다.
public class Article {

    /**
     * 게시글의 기본 키(PK).
     *
     * IDENTITY 전략을 사용하므로 INSERT 시 DB가 ID를 생성한다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 게시글 제목
    private String title;

    // 게시글 본문 또는 설명
    private String description;

    /**
     * 게시글이 최초로 저장된 시간을 자동 기록한다.
     * DemoApplication의 @EnableJpaAuditing과 함께 동작한다.
     */
    @CreatedDate
    private Date created;

    /**
     * 게시글이 수정될 때 마지막 수정 시간을 자동 갱신한다.
     */
    @LastModifiedDate
    private Date updated;

    /**
     * 게시글 작성자와 회원의 관계.
     *
     * 여러 Article이 하나의 Member를 참조할 수 있으므로 ManyToOne이다.
     * 즉, DB에서는 여러 게시글의 member_id가 하나의 회원 id를 가리킨다.
     *
     * @JoinColumn을 지정하여 Article 테이블에서 Member를 참조하는
     * 외래 키(FK) 컬럼 이름을 member_id로 명확하게 지정한다.
     */
    @ManyToOne
    @JoinColumn(name = "member_id") // Article 테이블의 FK 컬럼명을 member_id로 지정한다.
    private Member member;
}
