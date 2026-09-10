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
| [2026-09-10](./study/2026-09-10.md) | Spring Boot 배포 · JAR/WAR · Profile · H2 · REST API · 환경별 설정 · Bulletin Board 게시판 구현 |

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
├── study/            # 날짜별 학습 기록 및 Bulletin Board 정리
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

## 💼 Projects

1. 🎮 **GAMEHUB** → [`miniproject`](https://github.com/zuzusnasna/miniproject) 연결
2. 🏭 **Equipment Management** → [`equipment-management`](https://github.com/zuzusnasna/equipment-management) 연결
3. 📝 **Bulletin Board** → [`bulletin_board`](./bulletin_board) 연결

---

## 📝 Bulletin Board 실습

`bulletin_board`는 날짜별 학습 기록과 별도로 진행한 **Spring Boot 게시판 구현 실습 프로젝트**이다.

👉 [Bulletin Board 구현 원리 및 핵심 내용 정리](./study/Bulletin%20Board.md)

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

---

## 🎯 Goal

학습한 내용을 단순히 기록하는 데서 끝내지 않고, 직접 코드를 작성하고 정리하면서 **Spring Boot 기반 백엔드 개발 흐름을 이해하는 것**을 목표로 합니다.

날짜별 학습 기록은 `study/`에서 확인하고, 게시판 구현 원리와 핵심 내용은 `study/Bulletin Board.md`에서 확인할 수 있습니다.
