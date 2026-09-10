# 📝 Bulletin Board

Spring Boot를 기반으로 구현한 게시판 실습 프로젝트입니다.

단순한 CRUD 구현을 넘어 **Controller → Service → Repository → Database**의 전체 흐름과 **Spring Security를 이용한 인증·인가**를 직접 구현하는 것을 목표로 했습니다.

---

## 1. 프로젝트 구동 원리

```text
Browser
   ↓ HTTP Request
Controller
   ↓
Service
   ↓
Repository
   ↓
Database
   ↑
Repository
   ↑
Service (Entity → DTO)
   ↑
Controller → Model
   ↓
Thymeleaf
   ↓
Browser
```

| 계층 | 역할 |
|---|---|
| Controller | 요청을 받고 Service를 호출하며 View에 데이터를 전달 |
| Service | 비즈니스 로직 처리 및 Entity/DTO 변환 |
| Repository | JPA를 이용한 Database 조회·저장·수정·삭제 |
| Entity | Database 테이블과 매핑되는 객체 |
| DTO | 화면이나 요청에 필요한 데이터 전달 |
| Thymeleaf | Controller가 전달한 데이터를 HTML로 출력 |

---

## 2. 게시글 조회 흐름

`GET /article/list` 요청은 다음 순서로 동작합니다.

```text
Browser
  ↓
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
Entity → DTO
  ↓
Model
  ↓
article/list.html
  ↓
Browser
```

Controller가 직접 Database를 조회하지 않고 Service와 Repository를 거치는 것이 핵심입니다.

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
MemberUserDetails에서 현재 로그인 회원 확인
      ↓
Article Entity 생성
      ↓
ArticleRepository.save()
      ↓
Database 저장
```

게시글 작성에서 중요한 부분은 **현재 로그인한 회원과 게시글을 연결하는 것**입니다.

```text
로그인 사용자
    ↓
MemberUserDetails
    ↓
Member
    ↓
Article.member
```

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
수정 화면
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
Service
        ↓
Repository.delete()
        ↓
Database 삭제
```

수정·삭제처럼 데이터에 영향을 주는 작업은 Service에서 비즈니스 규칙을 처리한 뒤 Repository를 호출하는 구조로 이해합니다.

---

## 5. 회원가입과 BCrypt

회원가입에서는 평문 비밀번호를 Database에 그대로 저장하지 않습니다.

```text
사용자 입력
password = "1111"
       ↓
PasswordEncoder
       ↓
BCryptPasswordEncoder
       ↓
BCrypt 문자열
       ↓
Database 저장
```

핵심 코드는 다음과 같습니다.

```java
passwordEncoder.encode(memberForm.getPassword())
```

Spring Security 설정에서는 BCrypt Encoder를 Bean으로 등록합니다.

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

로그인에서는 다시 암호화해서 문자열을 직접 비교하지 않고 `matches()`로 검증합니다.

```java
passwordEncoder.matches(rawPassword, encodedPassword)
```

따라서 `data.sql`에 `1111`을 평문으로 넣는다고 자동으로 BCrypt로 변경되는 것은 아닙니다. Seed 데이터 역시 로그인에 사용할 경우 BCrypt 형식의 값을 저장해야 합니다.

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

현재 로그인한 사용자는 Controller에서 다음과 같이 받을 수 있습니다.

```java
@AuthenticationPrincipal MemberUserDetails userDetails
```

게시글 작성자 연결, 비밀번호 변경 등의 기능에 활용합니다.

---

## 7. 인증(Authentication)과 인가(Authorization)

**인증(Authentication)**은 "누구인가?"를 확인하는 과정입니다.

**인가(Authorization)**는 "이 사용자가 이 기능을 사용할 수 있는가?"를 확인하는 과정입니다.

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

예를 들어 `/member/list`는 `ROLE_ADMIN` 권한을 가진 사용자만 접근하도록 설정합니다.

---

## 8. 중요한 애너테이션

| 애너테이션 | 역할 |
|---|---|
| `@SpringBootApplication` | Spring Boot 애플리케이션 시작점 |
| `@Controller` | 웹 요청을 처리하는 Controller 지정 |
| `@GetMapping` | GET 요청 처리 |
| `@PostMapping` | POST 요청 처리 |
| `@Service` | 비즈니스 로직을 담당하는 Service 지정 |
| `@Entity` | JPA Entity 지정 및 DB 테이블 매핑 |
| `@Id` | Entity의 기본 키 지정 |
| `@GeneratedValue` | 기본 키 자동 생성 설정 |
| `@Repository` | 데이터 접근 계층 지정 |
| `@Valid` | 객체 Validation 실행 |
| `@AuthenticationPrincipal` | 현재 로그인 사용자 주입 |
| `@Transactional` | 여러 DB 작업을 하나의 트랜잭션으로 처리 |
| `@Bean` | Spring Container가 관리하는 객체 등록 |
| `@Configuration` | Spring 설정 클래스 지정 |

---

## 9. 중요한 메서드

### JpaRepository CRUD

```java
save(entity)
findById(id)
findAll()
delete(entity)
deleteById(id)
existsById(id)
```

### Optional

`findById()`처럼 결과가 없을 수 있는 조회에는 `Optional`이 사용됩니다.

```java
memberRepository.findById(id)
```

필요한 경우 다음과 같이 예외를 발생시킬 수 있습니다.

```java
.orElseThrow(...)
```

### PasswordEncoder

```java
encode(password)
matches(rawPassword, encodedPassword)
```

`encode()`는 비밀번호를 암호화하고, `matches()`는 입력값과 저장된 BCrypt 값을 검증합니다.

### Validation

```java
bindingResult.hasErrors()
bindingResult.rejectValue(...)
```

`hasErrors()`는 Validation 오류 여부를 확인하고, `rejectValue()`는 특정 필드에 직접 오류를 추가합니다.

---

## 10. DTO와 Entity

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

이 프로젝트에서는 Service에서 Entity와 DTO를 변환합니다.

---

## 11. Validation 흐름

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
오류 있음 → 입력 화면으로 이동
   ↓
오류 없음
   ↓
Service 실행
```

예:

```java
@Valid @ModelAttribute("article") ArticleForm articleForm
BindingResult bindingResult
```

자주 사용하는 Validation은 다음과 같습니다.

```java
@NotBlank
@Email
@Size
```

---

## 12. 페이징 처리

게시글이 많아지면 전체 데이터를 한 번에 가져오지 않고 페이지 단위로 조회합니다.

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

- `Pageable` : 페이지 번호와 페이지당 조회 개수 등을 표현
- `Page<T>` : 조회 결과와 페이지 정보를 함께 보관

---

## 13. 회원 삭제와 @Transactional

회원 삭제와 해당 회원의 게시글 삭제처럼 여러 DB 작업이 필요한 경우 하나의 트랜잭션으로 처리합니다.

```text
회원 삭제 요청
     ↓
게시글 삭제
     ↓
회원 삭제
     ↓
Commit
```

중간에 문제가 발생하면 Rollback하여 데이터가 일부만 삭제되는 상황을 방지할 수 있습니다.

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

## 15. 전체 기능 흐름

```text
                    Browser
                       ↓
                  Controller
                       ↓
                    Service
                 ↙          ↘
             Repository    Security
                 ↓             ↓
             Database    인증 / 인가

Validation
    ↓
@Valid → BindingResult

Password
    ↓
PasswordEncoder → BCrypt

Transaction
    ↓
@Transactional
```

---

## 16. 핵심적으로 이해할 내용

게시판 실습에서 특정 메서드를 외우는 것보다 **각 계층의 역할과 연결 관계를 이해하는 것**이 중요합니다.

### 반드시 이해하기

- `Controller → Service → Repository → Database` 흐름
- Entity와 DTO의 차이
- `JpaRepository`를 이용한 CRUD
- Service에서 비즈니스 로직 처리
- `@Valid` + `BindingResult` Validation
- `PasswordEncoder`와 BCrypt
- `@AuthenticationPrincipal`을 이용한 현재 로그인 사용자 확인
- 게시글과 Member의 연결
- Spring Security의 인증과 인가
- `ROLE_ADMIN`을 이용한 접근 제어
- `@Transactional`을 이용한 여러 DB 작업의 일관성 유지

### 한 문장으로 정리

> **Controller가 요청을 받고 → Service가 비즈니스 로직을 처리하고 → Repository가 Database를 다루며 → Spring Security가 인증·인가를 담당하는 구조로 게시판을 구현했다.**
