package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원 정보를 클라이언트에게 반환할 때 사용하는 Response DTO.
 *
 * 흐름:
 * DB -> Member Entity -> MemberService -> MemberResponse -> JSON 응답
 *
 * Request DTO와 Response DTO를 분리하면
 * 입력 데이터와 출력 데이터를 서로 독립적으로 관리할 수 있다.
 */
@Data // getter/setter 및 기본 메서드를 Lombok이 자동 생성
@Builder // Service에서 builder 패턴으로 Response 객체를 만들 수 있게 한다.
@AllArgsConstructor // 모든 필드를 받는 생성자 생성
@NoArgsConstructor // 기본 생성자 생성
public class MemberResponse {

    // DB에서 생성된 회원 PK
    private Long id;

    // 회원 이름
    private String name;

    // 회원 이메일
    private String email;

    // 회원 나이
    private Integer age;
}
