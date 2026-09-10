package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 회원의 권한 정보를 DB에 저장하기 위한 JPA Entity
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Authority {

    // 권한 데이터의 기본 키(PK)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 예: ROLE_ADMIN, ROLE_USER 같은 권한 문자열
    private String authority;

    // 여러 Authority가 하나의 Member에 연결될 수 있음
    @ManyToOne
    // Authority 테이블에 member_id라는 외래키(FK)를 생성하고 Member와 연결
    @JoinColumn(name = "member_id")
    private Member member;
}

// Member 1명 ←────── Authority 여러 개
//
// Authority 입장에서는 여러 Authority가 하나의 Member를 참조하므로 ManyToOne 관계
// Member 객체에는 OneToMany가 선언되어 있지 않기 때문에 현재는 단방향 관계
