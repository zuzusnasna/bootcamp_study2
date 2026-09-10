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
     게시판 실습
       ↓
     배포 / JAR · WAR
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
| [2026-09-10](./study/2026-09-10.md) | Spring Boot 배포 · JAR/WAR · Profile · H2 · REST API · 환경별 설정 · 게시판 실습 마무리 |

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
| `bulletin_board` | Spring Boot · MVC · JPA · Thymeleaf · Spring Security 기반 게시판 CRUD 실습 완료 |

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
└── bulletin_board/   # Spring Boot 게시판 CRUD · 인증/인가 실습
```

---

## 📝 Bulletin Board 실습 핵심

`bulletin_board` 프로젝트에서는 Spring Boot 게시판을 직접 구현하면서 다음 흐름을 학습했다.

```text
브라우저
   ↓ 요청
Controller
   ↓
Service
   ↓
Repository
   ↓
JPA
   ↓
Database

Database
   ↓
Entity
   ↓
Service : Entity → DTO
   ↓
Controller : Model
   ↓
Thymeleaf
   ↓
브라우저 화면
```

### 주요 기능

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

### 기능별 실행 확인 순서

| 순서 | 주소 | 확인 내용 | 로그인 |
|:---:|---|---|:---:|
| 1 | `/` | 게시글 목록으로 이동 | 불필요 |
| 2 | `/article/list` | 게시글 목록 / 페이징 | 불필요 |
| 3 | `/article/content?id=1` | 게시글 상세 | 불필요 |
| 4 | `/signup` | 회원가입 | 불필요 |
| 5 | `/login` | 로그인 | 불필요 |
| 6 | `/article/add` | 게시글 작성 | 필요 |
| 7 | `/article/edit?id=1` | 게시글 수정 | 필요 |
| 8 | `/password` | 비밀번호 변경 | 필요 |
| 9 | `/member/list` | 회원 목록 | `ROLE_ADMIN` |

자세한 실행 확인 순서와 마무리 체크리스트는 [2026-09-10 학습 기록](./study/2026-09-10.md)에서 확인할 수 있다.

---

## 🎯 Goal

학습한 내용을 단순히 기록하는 데서 끝내지 않고, 직접 코드를 작성하고 정리하면서 **Spring Boot 기반 백엔드 개발 흐름을 이해하는 것**을 목표로 합니다.

특히 게시판 실습을 통해 **Entity → Repository → Service → Controller → Thymeleaf** 흐름과 **Spring Security 인증·인가, BCrypt, Validation, CRUD, 페이징, `@Transactional`**이 실제 애플리케이션에서 어떻게 연결되는지 이해하는 것을 목표로 합니다.

날짜별 학습 기록은 `study/`에서 확인할 수 있습니다.
