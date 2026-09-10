package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

// 비밀번호 변경 화면에서 입력받은 데이터를 전달하기 위한 DTO이다.
// 기존 비밀번호와 새로운 비밀번호를 Entity와 분리하여 관리한다.
@Data
public class PasswordForm {

    // 사용자가 현재 사용 중인 기존 비밀번호를 입력하는 필드이다.
    // 비밀번호 변경 전에 기존 비밀번호가 맞는지 확인하기 위해 사용한다.
    @NotBlank(message = "기존 패스워드를 입력해 주세요")
    private String old;

    // 변경할 새로운 비밀번호를 입력하는 필드이다.
    // 최소 8글자 이상 입력하도록 검증한다.
    @Size(min = 8, message = "8글자 이상 입력해 주세요")
    @NotBlank(message = "새로운 패스워드를 입력해 주세요")
    private String password;

    // 새로운 비밀번호를 한 번 더 입력받아 오타를 확인하기 위한 필드이다.
    // 이 값은 DB에 저장하지 않고 입력값 검증에만 사용한다.
    @Size(min = 8, message = "8글자 이상 입력해 주세요")
    @NotBlank(message = "새로운 패스워드를 입력해 주세요")
    private String passwordConfirm;
}
