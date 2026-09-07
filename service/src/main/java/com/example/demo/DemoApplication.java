package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing //데이터 변경을 감시하고 추적하는 역할
//데이터 생성 날짜와 수정날짜 이외에도
//누가 최초로 생성했는지 누가 수정했는지를 자동으로 관리함을 위함
//@CreatedBy 와 @LastModifiedBy 애노테이션을 제공한다
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
