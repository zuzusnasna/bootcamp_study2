# 📝 Bulletin Board

Spring Boot를 기반으로 구현한 게시판 실습 프로젝트입니다.

단순히 게시글 CRUD를 구현하는 것에서 끝내지 않고 **Controller → Service → Repository → Database**로 이어지는 Spring Boot 애플리케이션의 전체 흐름과 **Spring Security를 이용한 인증·인가**를 직접 구현하는 것을 목표로 했습니다.

---

## 1. 프로젝트 구조

```text
Controller
   ↓
Service
   ↓
Repository
   ↓
Database
```

화면까지 포함하면 다음과 같은 흐름으로 동작합니다.

```text
[Browser]
    ↓ HTTP Request
[Controller]
    ↓
[Service]
    ↓
[Repository]
    ↓
[Database]
    ↑
[Repository]
    ↑
[Service]
    ↑ Entity → DTO 변환
    ↑
[Controller]
    ↓ Model
[Thymeleaf Template]
    ↓
[Browser]
```

### 각 계층의 역할

| 계층 | 역할 |
|---|---|
| Controller | URL 요청을 받고 Service를 호출하며 View에 데이터를 전달 |
| Service | 실제 비즈니스 로직 처리 및 Entity/DTO 변환 |
| Repository | JPA를 이용해 Database 조회·저장·수정·삭제 |
| Entity | Database 테이블과 연결되는 객체 |
| DTO | 화면이나 요청에 필요한 데이터만 전달 |
| Thymeleaf | Controller에서 전달받은 데이터를 HTML 화면에 출력 |

---

## 2. 게시글 조회 주요 흐름

예를 들어 `/article/list` 요청이 들어오면 다음 순서로 실행됩니다.

```text
브라우저
  ↓ GET /article/list
ArticleController
  ↓
ArticleService
  ↓
ArticleRepository
  ↓
Database
  ↓
Article Entity
  ↓
Service에서 DTO 변환
  ↓
Controller의 Model에 저장
  ↓
article/list.html
  ↓
브라우저 화면
```

### 핵심 포인트

**Controller는 직접 Database를 조회하지 않습니다.**

Controller는 요청을 받고 Service를 호출합니다.

Service는 필요한 비즈니스 로직을 처리하고 Repository를 통해 데이터를 가져옵니다.

Repository는 JPA를 이용하여 실제 Database 작업을 담당합니다.

이렇게 역할을 나누면 각각의 클래스가 하나의 역할에 집중할 수 있습니다.

---

## 3. 게시글 작성 흐름

```text
게시글 작성 화면
      ↓
POST /article/add
      ↓
ArticleController
      ↓
ArticleForm
      ↓
@Valid + BindingResult
      ↓
ArticleService
      ↓
로그인 사용자(MemberUserDetails)
      ↓
Article Entity 생성
      ↓
ArticleRepository.save()
      ↓
Database 저장
      ↓
게시글 목록으로 이동
```

게시글 작성에서 중요한 것은 **현재 로그인한 회원과 게시글을 연결하는 것**입니다.

```text
로그인 사용자
    ↓
MemberUserDetails
    ↓
Member
    ↓
Article.member
```

따라서 게시글을 작성한 회원의 `member_id`가 게시글에 연결됩니다.

---

## 4. 게시글 수정 / 삭제 흐름

### 수정

```text
GET /article/edit?id=1
        ↓
게시글 조회
        ↓
현재 로그인 사용자 확인
        ↓
수정 화면 출력
        ↓
POST /article/edit
        ↓
Validation
        ↓
Service
        ↓
Entity 수정
        ↓
Repository.save()
```

### 삭제

```text
POST /article/delete
        ↓
게시글 ID 확인
        ↓
게시글 존재 여부 확인
        ↓
Repository.delete()
        ↓
Database에서 삭제
```

수정이나 삭제처럼 데이터에 직접 영향을 주는 작업은 **Service에서 비즈니스 규칙을 확인한 뒤 Repository를 호출하는 구조**로 이해하는 것이 중요합니다.

---

## 5. 회원가입과 BCrypt

회원가입에서는 사용자가 입력한 비밀번호를 그대로 Database에 저장하지 않습니다.

```text
사용자 입력
password = "1111"
       ↓
PasswordEncoder
       ↓
BCryptPasswordEncoder
       ↓
$2a$... 또는 $2b$... 형태의 BCrypt 문자열
       ↓
Database 저장
```

핵심 코드는 다음과 같은 구조입니다.

```java
passwordEncoder.encode(memberForm.getPassword())
```

Spring Security 설정에서는 다음과 같이 BCrypt Encoder를 Bean으로 등록합니다.

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

### 로그인할 때

로그인 시에는 비밀번호를 다시 BCrypt 문자열로 바꾸어 비교하는 것이 아니라 `PasswordEncoder`가 입력값과 저장된 BCrypt 값을 검증합니다.

```text
입력 비밀번호
    ↓
PasswordEncoder.matches()
    ↓
Database의 BCrypt 비밀번호
    ↓
일치 여부 확인
```

따라서 `data.sql`에 `1111` 같은 평문을 넣는다고 자동으로 BCrypt로 변경되는 것은 아닙니다. Database에 저장할 값 자체가 BCrypt 형식이어야 합니다.

---

## 6. Spring Security 로그인 흐름

```text
로그인 화면
   ↓
POST /login
   ↓
Spring Security
   ↓
UserDetailsService
   ↓
MemberRepository
   ↓
Member 조회
   ↓
AuthorityRepository
   ↓
권한 조회
   ↓
MemberUserDetails 생성
   ↓
PasswordEncoder로 비밀번호 검증
   ↓
인증 성공
   ↓
SecurityContext에 인증 정보 저장
```

이 프로젝트에서는 `MemberUserDetails`를 통해 현재 로그인한 회원 정보를 사용할 수 있습니다.

```java
@AuthenticationPrincipal MemberUserDetails userDetails
```

Controller에서 현재 로그인 사용자를 직접 조회할 수 있기 때문에 게시글 작성자 연결, 비밀번호 변경 등의 기능에 활용할 수 있습니다.

---

## 7. Spring Security 인증과 인가

**인증(Authentication)**

> "누구인가?"

로그인한 사용자가 실제 회원인지 확인하는 과정입니다.

**인가(Authorization)**

> "이 사용자가 이 기능을 사용할 수 있는가?"

예를 들어 관리자 회원 목록은 `ROLE_ADMIN` 권한을 가진 사용자만 접근할 수 있도록 설정합니다.

```text
로그인
  ↓
인증(Authentication)
  ↓
권한 확인
  ↓
인가(Authorization)
  ↓
허용 / 거부
```

---

## 8. 중요한 애너테이션

| 애너테이션 | 역할 |
|---|---|
| `@SpringBootApplication` | Spring Boot 애플리케이션 시작점 |
| `@Controller` | 웹 요청을 처리하는 Controller 지정 |
| `@GetMapping` | GET 요청 처리 |
| `@PostMapping` | POST 요청 처리 |
| `@Service` | 비즈니스 로직을 담당하는 Service 지정 |
| `@Entity` | JPA Entity로 지정하여 DB 테이블과 매핑 |
| `@Id` | Entity의 기본 키 지정 |
| `@GeneratedValue` | 기본 키 자동 생성 설정 |
| `@Repository` | 데이터 접근 계층을 나타냄 |
| `@Valid` | 객체의 Validation 실행 |
| `@AuthenticationPrincipal` | 현재 로그인한 사용자를 Controller에 주입 |
| `@Transactional` | 여러 DB 작업을 하나의 트랜잭션으로 처리 |
| `@Bean` | Spring Container가 관리하는 객체 등록 |
| `@Configuration` | Spring 설정 클래스 지정 |

---

## 9. 중요한 메서드

### Repository

`JpaRepository`를 상속하면 기본적인 CRUD 메서드를 사용할 수 있습니다.

```java
save(entity)
findById(id)
findAll()
delete(entity)
deleteById(id)
existsById(id)
```

### Optional

`findById()`와 같이 결과가 없을 수도 있는 조회에는 `Optional`이 사용됩니다.

```java
memberRepository.findById(id)
```

필요한 경우:

```java
.orElseThrow(...)
```

를 사용하여 데이터가 없을 때 예외를 발생시킬 수 있습니다.

### PasswordEncoder

```java
encode(password)
```

입력한 비밀번호를 BCrypt 등으로 암호화합니다.

```java
matches(rawPassword, encodedPassword)
```

입력 비밀번호와 Database에 저장된 암호화 비밀번호가 일치하는지 확인합니다.

### Validation

```java
bindingResult.hasErrors()
```

Validation 오류가 있는지 확인합니다.

```java
bindingResult.rejectValue(...)
```

특정 필드에 직접 오류를 추가할 수 있습니다.

---

## 10. DTO와 Entity의 역할

### Entity

Database와 직접 연결되는 객체입니다.

```text
Article Entity
      ↕
ARTICLE Table
```

### DTO

화면이나 요청에 필요한 데이터만 전달하기 위한 객체입니다.

```text
Entity
  ↓
DTO
  ↓
Controller / View
```

이 프로젝트에서는 Service에서 Entity와 DTO를 변환하는 구조를 사용합니다.

```text
Database
   ↓
Entity
   ↓
Service
   ↓
DTO
   ↓
Controller
   ↓
View
```

---

## 11. Validation 흐름

회원가입이나 게시글 작성에서는 `@Valid`와 `BindingResult`를 함께 사용합니다.

```java
public String add(
        @Valid @ModelAttribute("article") ArticleForm articleForm,
        BindingResult bindingResult) {
```

흐름은 다음과 같습니다.

```text
사용자 입력
   ↓
Form DTO
   ↓
@Valid
   ↓
Validation 검사
   ↓
BindingResult
   ↓
오류 있음 ─→ 입력 화면으로 다시 이동
   ↓
오류 없음
   ↓
Service 실행
```

자주 사용하는 Validation은 다음과 같습니다.

```java
@NotBlank
@Email
@Size
```

---

## 12. 페이징 처리

게시글이 많아지면 모든 데이터를 한 번에 가져오는 대신 페이지 단위로 조회합니다.

```text
Database
   ↓
JpaRepository
   ↓
Pageable
   ↓
Page<Article>
   ↓
Controller
   ↓
Thymeleaf
```

핵심 객체는 다음 두 가지입니다.

```java
Pageable
Page<T>
```

`Pageable`은 **몇 번째 페이지를 몇 개씩 조회할지**를 나타내고, `Page<T>`는 조회 결과와 페이지 정보를 함께 가지고 있습니다.

---

## 13. 회원 삭제와 @Transactional

회원 삭제 시 해당 회원이 작성한 게시글도 함께 처리해야 한다면 여러 Database 작업이 발생합니다.

```text
회원 삭제 요청
     ↓
회원의 게시글 삭제
     ↓
회원 삭제
```

이 작업을 하나의 단위로 처리하기 위해 `@Transactional`을 사용합니다.

```text
Transaction 시작
      ↓
게시글 삭제
      ↓
회원 삭제
      ↓
정상 완료 → Commit
      ↓
문제 발생 → Rollback
```

즉, 여러 DB 작업을 **하나의 작업 단위로 묶어서 데이터의 일관성을 유지**하는 것이 핵심입니다.

---

## 14. 로그아웃 흐름

이 프로젝트에서는 로그아웃 확인 화면과 실제 Spring Security 로그아웃 처리를 구분했습니다.

```text
GET /logout
   ↓
로그아웃 확인 화면
   ↓
POST /logout
   ↓
Spring Security
   ↓
Session 무효화
   ↓
Authentication 제거
   ↓
/login 이동
```

---

## 15. 전체 기능 흐름 한눈에 보기

```text
                    ┌──────────────┐
                    │    Browser   │
                    └──────┬───────┘
                           │
                           ↓
                    ┌──────────────┐
                    │  Controller  │
                    └──────┬───────┘
                           │
                           ↓
                    ┌──────────────┐
                    │    Service   │
                    │ 비즈니스 로직 │
                    └──────┬───────┘
                           │
                           ↓
                    ┌──────────────┐
                    │  Repository  │
                    │    JPA       │
                    └──────┬───────┘
                           │
                           ↓
                    ┌──────────────┐
                    │   Database   │
                    └──────────────┘

Spring Security
      ↓
인증 → UserDetailsService → Member
      ↓
인가 → ROLE_ADMIN 등 권한 확인

Validation
      ↓
@Valid → BindingResult

Password
      ↓
PasswordEncoder → BCrypt

Transaction
      ↓
@Transactional → 여러 DB 작업을 하나의 단위로 처리
```

---

## 16. 핵심적으로 이해할 내용

이 게시판 실습에서 가장 중요한 것은 특정 HTML 문법이나 메서드를 외우는 것이 아니라 **각 계층이 어떤 역할을 하고 서로 어떻게 연결되는지 이해하는 것**입니다.

### 반드시 이해하기

- `Controller → Service → Repository → Database` 흐름
- Entity와 DTO의 차이
- `JpaRepository`를 이용한 CRUD
- Service에서 비즈니스 로직 처리
- `@Valid` + `BindingResult` Validation 흐름
- `PasswordEncoder`와 BCrypt를 이용한 비밀번호 처리
- `UserDetailsService`와 `MemberUserDetails`를 이용한 로그인 사용자 관리
- 인증(Authentication)과 인가(Authorization)의 차이
- `ROLE_ADMIN`을 이용한 관리자 접근 제어
- `@AuthenticationPrincipal`로 현재 로그인 사용자 사용
- `@Transactional`을 이용한 여러 DB 작업의 원자적 처리
- `Pageable`과 `Page`를 이용한 페이징

### 전체적으로 기억할 한 문장

> **사용자의 요청을 Controller가 받고, Service가 비즈니스 로직을 처리하고, Repository가 JPA를 통해 Database와 통신한 뒤, 결과를 DTO와 View를 통해 사용자에게 보여주는 구조이다.**
