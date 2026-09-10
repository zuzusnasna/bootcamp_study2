package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 회원 정보를 DB에 저장하기 위한 JPA Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Member {

    // 회원의 기본 키(PK)
    @Id
    // 회원 객체가 DB에 저장될 때 ID를 자동으로 생성
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 회원 이름
    private String name;

    // 로그인에 사용하는 이메일
    private String email;

    // BCrypt 등으로 암호화해서 저장할 비밀번호
    private String password;
}
