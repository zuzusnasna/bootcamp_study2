package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 회원가입/회원정보 입력 화면에서 전달받는 값을 담아두는 DTO이다.
//
// Entity를 화면과 직접 연결하지 않고 별도의 Form 객체를 사용하는 이유는
// 화면에서 입력받는 값과 실제 DB에 저장하는 회원 정보를 분리하기 위해서이다.
// 특히 회원가입 과정에서는 비밀번호 확인처럼 DB에 저장할 필요가 없는 값도
// 입력받아야 하기 때문에 MemberForm과 같은 별도의 객체가 필요하다.
@Data
// getter/setter, toString, equals, hashCode 등을 자동으로 만들어 준다.
@Builder
// MemberForm 객체를 필요한 값만 넣어서 생성할 수 있도록 Builder 패턴을 제공한다.
@AllArgsConstructor
// 모든 필드를 매개변수로 받는 생성자를 자동으로 만들어 준다.
@NoArgsConstructor
// 기본 생성자를 자동으로 만들어 준다.
public class MemberForm {

    // 회원 수정 등의 상황에서 기존 회원을 구분하기 위한 ID이다.
    private Long id;

    // 회원 이름은 반드시 입력해야 한다.
    // @NotBlank는 null, 빈 문자열, 공백만 입력된 경우를 모두 허용하지 않는다.
    @NotBlank(message = "이름을 입력하세요")
    private String name;

    // 이메일은 반드시 입력해야 하고 이메일 형식도 확인한다.
    @NotBlank(message = "이메일을 입력하세요")
    @Email(message = "이메일 형식이 잘못 되었습니다.")
    private String email;

    // 회원가입 시 입력받을 비밀번호이다.
    private String password;

    // 사용자가 입력한 비밀번호를 한 번 더 확인하기 위한 값이다.
    // DB에 저장하기 위한 회원 정보라기보다 회원가입 과정에서 입력값을 검증하기 위해 사용한다.
    private String passwordConfirm;
}
