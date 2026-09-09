# Spring Security 인증·인가 실습 완료

Spring Security를 이용한 로그인 인증과 사용자 권한 인가 실습.

- `UserDetailsService`를 구현하여 DB의 회원 정보를 조회하고 `UserDetails`로 변환
- 이메일을 로그인 식별자로 사용
- `PasswordEncoder`와 `BCryptPasswordEncoder`를 이용한 비밀번호 해시 처리
- `SecurityFilterChain`을 이용한 URL별 접근 권한 설정
- `ROLE_USER`, `ROLE_ADMIN` 권한에 따른 접근 제어
- Form Login / Logout 기본 설정
