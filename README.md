# 📚 Spring Boot Bootcamp Study

Spring Boot 기반의 학습 내용을 날짜별로 기록하고, 실습 코드를 함께 관리하는 저장소입니다.

> **Study Log + Practice Code**

---

## 🗺️ Learning Roadmap

```text
Java
  ↓
Spring Boot
  ├─ DI / IoC
  ├─ AOP
  ├─ Lombok
  ├─ JDBC
  ├─ MyBatis
  ├─ JPA
  ├─ Thymeleaf / Spring MVC
  └─ Spring Security
       ↓
     CRUD / 인증·인가
       ↓
     Profile / 환경별 설정
       ↓
     배포 / JAR · WAR
       ↓
     게시판 실습
```

---

## 📅 Study Log

| 날짜 | 학습 내용 |
|:---:|---|
| [2026-09-03](./study/2026-09-03.md) | AOP · DI · Lombok |
| [2026-09-04](./study/2026-09-04.md) | JDBC · MyBatis |
| [2026-09-07](./study/2026-09-07.md) | JPA · CRUD |
| [2026-09-08](./study/2026-09-08.md) | JSP · MVC · RESTful API · Thymeleaf · JPA 연관관계 |
| [2026-09-09](./study/2026-09-09.md) | 회원 수정 · Spring Security 인증/인가 · UserDetailsService · BCrypt |
| [2026-09-10](./study/2026-09-10.md) | Spring Boot 배포 · JAR/WAR · Profile · H2 · REST API · 환경별 설정 |

---

## 💻 Practice Code

| 폴더 | 내용 |
|---|---|
| `aop` | AOP 실습 완료 |
| `di` | DI / IoC 실습 완료 |
| `jdbc` | JDBC 실습 완료 |
| `jpa` | JPA 실습 완료 |
| `lombok` | Lombok 실습 완료 |
| `mybatis` | MyBatis 실습 완료 |
| `service` | Spring Data JPA · RESTful API 실습 완료 |
| `thymleaf` | Thymeleaf · Spring MVC 실습 완료 |
| `mvc1` | Spring MVC 실습 |
| `mvc2` | Spring MVC 실습 완료 |
| `security1` | Spring Security 인증 · 인가 실습 완료 |
| `deployment` | Spring Boot JAR · WAR · Profile · H2 및 외부 Tomcat 배포 실습 |
| `bulletin_board` | Spring Boot · MVC · JPA · Thymeleaf · Spring Security 기반 게시판 실습 완료 |

---

## 🛠️ Tech Stack

- **Language** · Java
- **Framework** · Spring Boot
- **Web** · Spring MVC · Thymeleaf
- **Security** · Spring Security · Authentication · Authorization
- **Persistence** · JPA · MyBatis · JDBC
- **Database** · H2
- **Build** · Gradle
- **Deployment** · JAR · WAR · Tomcat
- **Configuration** · Spring Profile · `spring.profiles.active`
- **IDE** · IntelliJ IDEA
- **Version Control** · Git · GitHub

---

## 📂 Repository Structure

```text
bootcamp_study2/
├── study/            # 날짜별 학습 기록
├── aop/              # AOP 실습
├── di/               # DI / IoC 실습
├── jdbc/             # JDBC 실습
├── jpa/              # JPA 실습
├── lombok/           # Lombok 실습
├── mybatis/          # MyBatis 실습
├── service/          # Spring Data JPA · RESTful API 실습
├── thymleaf/         # Thymeleaf · Spring MVC 실습
├── mvc1/             # Spring MVC 실습
├── mvc2/             # Spring MVC 실습
├── security1/        # Spring Security 인증 · 인가 실습
├── deployment/       # Spring Boot JAR · WAR · Profile 및 Tomcat 배포 실습
└── bulletin_board/   # Spring Boot 게시판 실습
```

---

## 📝 Bulletin Board 실습

`bulletin_board`는 날짜별 학습 기록과 별도로 진행한 **Spring Boot 게시판 구현 실습 프로젝트**이다.

### 구현 기능

- 게시글 목록 및 페이징
- 게시글 상세 조회
- 회원가입
- BCrypt 기반 비밀번호 암호화
- Spring Security 로그인 / 로그아웃
- 현재 로그인 사용자와 게시글 작성자 연결
- 게시글 작성 / 수정 / 삭제
- 비밀번호 변경
- `ROLE_ADMIN` 관리자 회원 목록 접근 제어
- 회원 삭제 시 작성 게시글을 먼저 삭제하는 `@Transactional` 처리
- Validation 및 `BindingResult`를 이용한 입력값 검증

### 23. 기능별 실행 확인 순서

| 순서 | 주소 | 확인 내용 | 로그인 |
|:---:|---|---|:---:|
| 1 | `http://localhost:8080/` | 게시글 목록으로 이동 | 불필요 |
| 2 | `/article/list` | 게시글 목록 / 페이징 | 불필요 |
| 3 | `/article/content?id=1` | 게시글 상세 | 불필요 |
| 4 | `/signup` | 회원가입 | 불필요 |
| 5 | `/login` | 로그인 | 불필요 |
| 6 | `/article/add` | 게시글 작성 | 필요 |
| 7 | `/article/edit?id=1` | 게시글 수정 | 필요 |
| 8 | `/password` | 비밀번호 변경 | 필요 |
| 9 | `/member/list` | 회원 목록 | `ROLE_ADMIN` |

### 24. 꼭 이해해야 할 핵심 흐름

#### MVC + Service + Repository

```text
브라우저
   ↓ 요청
Controller
   ↓
Service
   ↓
Repository
   ↓
Database
```

#### 조회 결과

```text
Database
   ↓
Repository : Entity
   ↓
Service : Entity → DTO
   ↓
Controller : Model에 DTO 저장
   ↓
templates/*.html
   ↓
브라우저 화면
```

게시판 실습의 핵심은 HTML 문법 자체가 아니라 **Controller-Service-Repository-JPA-Security가 실제 애플리케이션에서 어떻게 연결되는지 이해하는 것**이다.

### 25. 반복해서 확인할 개념

| 단계 | 파일 / 작업 | 핵심 내용 |
|:---:|---|---|
| 1 | `@Entity` | 자바 객체와 DB 테이블 연결 |
| 2 | DTO | Entity에서 필요한 데이터만 화면으로 전달 |
| 3 | `JpaRepository` | 기본 CRUD와 메서드 이름 기반 쿼리 |
| 4 | `@Service` | 비즈니스 로직과 DTO 변환 |
| 5 | `@Controller` | URL 요청 처리 + Model + View 이름 반환 |
| 6 | `@Valid` | DTO의 Validation 애노테이션 실행 |
| 7 | `BindingResult` | 검증 오류 저장 및 화면 재표시 |
| 8 | `PasswordEncoder` | 비밀번호 BCrypt 인코딩 / 검증 |
| 9 | `@AuthenticationPrincipal` | 현재 로그인 사용자 정보 주입 |
| 10 | `Pageable` / `Page` | 게시글 / 회원 목록 페이징 |
| 11 | `@Transactional` | 회원 삭제처럼 여러 DB 작업을 한 단위로 처리 |
| 12 | `ROLE_ADMIN` | 관리자 URL 접근 제어 |

### 26. 축소해도 되는 부분

| 단계 | 파일 / 작업 | 핵심 내용 |
|:---:|---|---|
| 축소 1 | Thymeleaf 레이아웃 문법 | templates를 복사하므로 결과 화면 확인 위주 |
| 축소 2 | 페이징 버튼 계산식 | 동작 원리와 결과 확인 위주 |
| 축소 3 | `Page` 객체의 핵심 속성 | 필요한 속성 중심으로 설명 |
| 축소 4 | Validation 애노테이션 전체 목록 | `@NotBlank`, `@Email`, `@Size` 중심 |
| 축소 5 | `UserDetails` 세부 메서드 | username / password / authorities / memberId 중심 |
| 축소 6 | Bootstrap | 디자인 도구라는 정도만 이해 |

반대로 **Entity-DTO 변환, Repository, Service, 로그인 사용자와 memberId 연결, PasswordEncoder, 게시글 CRUD, ROLE_ADMIN, @Transactional**은 핵심 내용이다.

### 27. 수업 마무리 체크리스트

- [ ] 애플리케이션 실행 시 `schema.sql` / `data.sql` 오류가 없는가?
- [ ] H2 콘솔에서 `MEMBER`, `AUTHORITY`, `ARTICLE` 테이블을 확인했는가?
- [ ] 비로그인 상태에서 게시글 목록 / 상세를 볼 수 있는가?
- [ ] 회원가입 후 비밀번호가 평문이 아닌 BCrypt 문자열로 저장되는가?
- [ ] 로그인 후 게시글을 작성할 수 있는가?
- [ ] 작성된 게시글의 `member_id`가 로그인 회원 ID와 연결되는가?
- [ ] 게시글 수정 / 삭제가 동작하는가?
- [ ] `ROLE_ADMIN` 계정만 `/member/list`에 접근 가능한가?
- [ ] 회원 삭제 시 해당 회원의 게시글도 먼저 삭제되는가?
- [ ] `templates` 폴더가 `src/main/resources/templates` 경로에 정확히 복사되어 있는가?

---

## 🚀 주요 작업물

지금까지 학습 과정에서 직접 구현한 주요 프로젝트를 정리했습니다.

| 작업물 | 설명 | 주요 기술 | Repository |
|---|---|---|---|
| 🎮 **GAMEHUB** | 게임 카테고리 기반 커뮤니티 웹 애플리케이션 | Java · Servlet · Oracle · JavaScript | [GitHub](https://github.com/zuzusnasna/miniproject) |
| 🏭 **Equipment Management** | 장비 등록·조회·수정·삭제 및 검색·필터 기능을 제공하는 장비 관리 시스템 | Spring Boot · JPA · Oracle · React | [GitHub](https://github.com/zuzusnasna/equipment-management) |
| 📝 **Bulletin Board** | Spring Boot 기반 게시판 구현 실습 | Spring Boot · JPA · Thymeleaf · Spring Security · H2 | [소스 코드](./bulletin_board) |

### 📝 Bulletin Board 구현

게시판 구현은 단순 CRUD를 넘어 **Spring Boot 웹 애플리케이션의 전체 흐름을 직접 구현하는 것을 목표**로 진행했습니다.

- 회원가입 / 로그인 / 로그아웃
- BCrypt 비밀번호 암호화
- 게시글 CRUD 및 페이징
- 작성자 기반 게시글 수정 / 삭제
- `ROLE_ADMIN` 관리자 권한 처리
- Validation / `BindingResult`
- `@AuthenticationPrincipal`을 이용한 로그인 사용자 연결
- `@Transactional`을 이용한 회원 및 게시글 삭제 처리
- Controller → Service → Repository → Database 구조 이해

자세한 구현 내용과 실행 확인 과정은 아래 게시판 실습 문서에서 확인할 수 있습니다.

➡️ [게시판 구현 실습 상세 내용](#-bulletin-board-실습)

---

## 🎯 Goal

학습한 내용을 단순히 기록하는 데서 끝내지 않고, 직접 코드를 작성하고 정리하면서 **Spring Boot 기반 백엔드 개발 흐름을 이해하는 것**을 목표로 합니다.

게시판 실습에서는 **Entity → Repository → Service → Controller → Thymeleaf** 흐름과 **Spring Security 인증·인가, BCrypt, Validation, CRUD, 페이징, `@Transactional`**이 실제 애플리케이션에서 어떻게 연결되는지 이해하는 것을 목표로 합니다.

날짜별 학습 기록은 `study/`에서 확인하고, 게시판 구현 과정은 `bulletin_board/`에서 확인할 수 있습니다.
