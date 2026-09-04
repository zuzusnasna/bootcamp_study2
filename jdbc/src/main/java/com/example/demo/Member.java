package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table  //애노테이션이 사용된 클래스와 데이터베이스 테이블을 매핑할 때의 클래스 이름은
              // 테이블 이름으로 매핑하고,
              //프로퍼티 이름은 컬럼이름으로 매핑.
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id //데이터 베이스 테이블과 연동하는 객체임을 선언
    private Long id;
    private String name;
    private String email;
    private Integer age;
}
