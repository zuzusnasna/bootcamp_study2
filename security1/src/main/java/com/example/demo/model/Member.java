package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 회원 정보를 DB 테이블과 매핑하는 JPA 엔티티
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK 자동 생성
    private Long id;
    private String name;
    private String email; // 이 실습에서는 로그인 식별자로 사용
    private Integer age;
    private String password; // BCrypt로 해시된 비밀번호 저장
    // 회원 한 명이 하나의 권한을 가진다고 가정한 실습 구조
    private String authority;
}
