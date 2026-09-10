package com.example.demo.bcrypt;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// BCrypt 방식으로 평문 비밀번호를 암호화해 보는 테스트용 클래스이다.
// 회원가입의 실제 비밀번호 암호화는 MemberService에서 PasswordEncoder를 사용해 처리한다.
public class BCryptPassWord {

    // 테스트 프로그램의 시작점이다.
    public static void main(String[] args) {
        // BCrypt 방식의 비밀번호 암호화 객체를 생성한다.
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // 입력한 평문 비밀번호를 BCrypt 문자열로 암호화하여 콘솔에 출력한다.
        // 같은 비밀번호라도 실행할 때마다 Salt가 달라져 결과 문자열은 달라질 수 있다.
        System.out.println(encoder.encode("1111"));
    }
}
