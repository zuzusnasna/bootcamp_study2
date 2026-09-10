package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// Spring Boot 애플리케이션의 시작점이 되는 클래스이다.
@SpringBootApplication
// Entity의 생성일과 수정일을 자동으로 기록하는 JPA Auditing 기능을 활성화한다.
@EnableJpaAuditing
public class DemoApplication {

    // Java 애플리케이션이 실행될 때 가장 먼저 호출되는 메서드이다.
    public static void main(String[] args) {
        // Spring Boot 애플리케이션을 실행하고 Spring 컨테이너를 생성한다.
        SpringApplication.run(DemoApplication.class, args);
    }
}
