# Bulletin Board Troubleshooting

Spring Boot 게시판 구현 과정에서 실제로 발생했던 오류와 해결 과정을 정리합니다.

## 1. Thymeleaf 상세 페이지 SpEL `id` 오류

### 문제
게시글 상세 페이지에서 다음과 같은 오류가 발생했습니다.

```text
org.springframework.expression.spel.SpelEvaluationException: EL1008E
Property or field 'id' cannot be found
```

### 원인
Thymeleaf에서 사용하는 객체의 실제 필드명과 템플릿에서 접근한 프로퍼티명이 일치하지 않았습니다.

### 해결
컨트롤러에서 전달하는 모델 객체와 DTO/Entity의 필드명을 확인하고, Thymeleaf 표현식이 실제 프로퍼티를 사용하도록 수정했습니다.

### 배운 점
Thymeleaf의 `th:*` 표현식은 단순 문자열이 아니라 서버에서 전달된 객체의 Java 프로퍼티를 기준으로 동작하므로 DTO와 템플릿의 필드명을 일치시키는 것이 중요합니다.

## 2. Thymeleaf Spring Security `sec` 네임스페이스 오류

### 문제
템플릿에서 `sec:authorize`를 사용했을 때 다음과 같은 문제가 발생했습니다.

```text
네임스페이스 'sec'이(가) 바인딩되지 않았습니다.
```

### 원인
Thymeleaf에서 Spring Security 전용 속성을 사용하기 위한 설정과 네임스페이스가 맞지 않았습니다.

### 해결
Spring Security Thymeleaf 연동 설정을 확인하고 HTML에 다음 네임스페이스를 선언했습니다.

```html
xmlns:sec="http://www.thymeleaf.org/extras/spring-security"
```

또한 프로젝트의 Thymeleaf Spring Security 의존성을 확인했습니다.

### 배운 점
Spring Security의 인증 정보를 Thymeleaf에서 사용하려면 Spring Security와 Thymeleaf 사이의 연동 설정이 필요합니다.

## 3. Thymeleaf `memberId` 프로퍼티 인식 오류

### 문제
게시글 작성자와 로그인 사용자를 비교하는 과정에서 `memberId`를 해결하지 못하는 문제가 발생했습니다.

### 원인
템플릿에서 참조하는 변수명과 실제 모델에 전달된 객체의 프로퍼티 구조가 일치하지 않았습니다.

### 해결
컨트롤러에서 모델에 전달하는 값과 DTO/Entity의 필드명을 다시 확인하고, 실제 객체 구조에 맞게 Thymeleaf 표현식을 수정했습니다.

### 배운 점
템플릿 오류가 발생하면 HTML만 확인하지 않고 Controller → Model → DTO/Entity의 데이터 전달 구조를 함께 확인해야 합니다.

## 4. 게시글 수정 기능 오류

### 문제
게시글 수정 화면까지 이동했지만 수정 요청이 정상적으로 처리되지 않는 문제가 발생했습니다.

### 원인
수정 요청에서 사용하는 게시글 식별자와 Controller의 파라미터 및 수정 대상 Entity 연결 과정이 일치하지 않았습니다.

### 해결
수정 페이지에서 게시글 ID가 정상적으로 전달되는지 확인하고, Controller → Service → Repository의 수정 흐름을 연결했습니다.

### 배운 점
CRUD 기능은 화면만 구현하는 것이 아니라 식별자(ID)가 각 계층을 거쳐 정확하게 전달되는지 확인해야 합니다.

## 5. BCrypt 비밀번호 암호화와 `data.sql`

### 문제
`data.sql`에 평문 비밀번호를 넣으면 Spring Security 로그인 과정에서 자동으로 BCrypt 암호화가 될 것이라고 생각했습니다.

### 원인
`data.sql`은 데이터베이스에 초기 데이터를 직접 입력할 뿐, 애플리케이션의 `PasswordEncoder`를 자동으로 호출하지 않습니다.

### 해결
초기 회원 데이터도 로그인에서 사용하는 BCrypt 형식의 해시값으로 저장해야 합니다. 회원가입처럼 애플리케이션 코드에서 저장하는 경우에는 `PasswordEncoder`를 통해 암호화한 뒤 저장합니다.

### 배운 점
비밀번호 암호화는 DB 컬럼의 기능이 아니라 애플리케이션의 인증 흐름에서 처리되는 로직이라는 점을 이해했습니다.

## 6. `Optional`과 `NoSuchElementException`

### 문제
회원 또는 게시글을 조회하는 과정에서 다음 오류가 발생했습니다.

```text
java.util.NoSuchElementException: No value present
```

### 원인
`Optional`에 값이 없는 상태에서 `get()`을 호출했기 때문입니다.

### 해결
조회 결과가 없을 수 있다는 것을 전제로 `orElseThrow()` 등으로 예외 상황을 명확하게 처리하고, 필요한 경우 조회 대상 ID가 실제 데이터에 존재하는지도 확인했습니다.

### 배운 점
`Optional`은 값이 항상 존재한다는 가정으로 사용하는 것이 아니라, 조회 결과가 없을 수 있는 상황을 코드에서 명시적으로 처리하기 위한 도구입니다.

## 7. 게시판 구현에서 확인한 공통 문제 해결 방법

게시판 오류를 해결하면서 다음 순서로 원인을 추적하는 습관을 만들었습니다.

1. 브라우저에서 발생한 오류 메시지 확인
2. 서버 콘솔의 예외 및 Stack Trace 확인
3. Controller의 요청 파라미터와 Model 확인
4. DTO/Entity의 필드명 확인
5. Service와 Repository의 데이터 조회·수정 흐름 확인
6. Thymeleaf 표현식과 실제 객체 구조 비교
7. 수정 후 다시 테스트하고 Git에 커밋

## 8. 정리

게시판 구현 과정에서 발생한 오류를 단순히 수정하는 데 그치지 않고, **Controller → Service → Repository → Entity/DB → Thymeleaf**로 이어지는 데이터 흐름을 따라가며 원인을 찾는 방법을 익혔습니다.
