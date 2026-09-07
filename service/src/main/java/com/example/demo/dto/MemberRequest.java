package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원 생성/수정 요청에 사용하는 Request DTO.
 *
 * 흐름:
 * Client의 JSON 요청 -> MemberRequest -> MemberService -> Member Entity
 *
 * Entity를 HTTP 요청에 직접 사용하지 않고 DTO를 사용하는 이유는
 * 외부에서 전달받을 데이터와 DB에 저장하는 데이터를 분리하기 위해서다.
 */
@Data // getter/setter, toString, equals, hashCode 등을 Lombok이 자동 생성
@Builder // 객체를 builder 방식으로 생성할 수 있게 한다.
@AllArgsConstructor // 모든 필드를 받는 생성자 생성
@NoArgsConstructor // JSON 역직렬화를 위해 기본 생성자 생성
public class MemberRequest {

    // 회원 이름: 요청 JSON의 name 값과 연결된다.
    private String name;

    // 회원 이메일: 요청 JSON의 email 값과 연결된다.
    private String email;

    // 회원 나이: 요청 JSON의 age 값과 연결된다.
    private Integer age;
}
