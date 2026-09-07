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
 * 핵심 관계:
 * Article N : 1 Member
 * 하나의 회원(Member)은 여러 게시글(Article)을 작성할 수 있다.
 * 따라서 Article이 작성자 Member를 참조한다.
 */
@Entity // JPA가 관리하는 Entity로 등록
@Data // getter/setter 등 반복적인 메서드를 Lombok이 생성
@Builder // builder 방식으로 Entity 생성 가능
@AllArgsConstructor // 모든 필드를 받는 생성자 생성
@NoArgsConstructor // JPA용 기본 생성자
@EntityListeners(AuditingEntityListener.class) // 생성/수정 시간 자동 기록을 위한 Listener
public class Article {

    // 게시글 PK. DB가 INSERT 시 ID를 자동 생성한다.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 게시글 제목
    private String title;

    // 게시글 본문/설명
    private String description;

    /**
     * Entity가 처음 저장될 때 생성 시간이 자동으로 기록된다.
     * DemoApplication의 @EnableJpaAuditing과 함께 동작한다.
     */
    @CreatedDate
    private Date created;

    /**
     * Entity가 수정될 때 마지막 수정 시간이 자동으로 갱신된다.
     */
    @LastModifiedDate
    private Date updated;

    /**
     * 게시글 작성자와 회원의 관계.
     * 여러 Article이 하나의 Member를 참조할 수 있으므로 ManyToOne이다.
     */
    @ManyToOne
    private Member member;
}
