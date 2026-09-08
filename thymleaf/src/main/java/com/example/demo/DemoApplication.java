package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 애플리케이션의 시작점.
 *
 * @SpringBootApplication이 포함된 클래스를 실행하면
 * Spring Boot가 애플리케이션 컨텍스트를 생성하고 내장 서버를 실행한다.
 */
@SpringBootApplication // Spring Boot 설정과 컴포넌트 스캔을 활성화한다.
public class DemoApplication {

    /**
     * 애플리케이션을 실행하는 main 메서드.
     */
    public static void main(String[] args) {
        // Spring Boot 애플리케이션을 시작한다.
        SpringApplication.run(DemoApplication.class, args);
    }

}
