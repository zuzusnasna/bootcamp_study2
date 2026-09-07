package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 요청한 데이터를 찾을 수 없을 때 사용하는 예외.
 *
 * 예를 들어 Repository에서 id로 회원/게시글을 조회했는데
 * 해당 데이터가 존재하지 않으면 Service에서 이 예외를 발생시킨다.
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Not Found")
// 이 예외가 발생하면 Spring MVC가 HTTP 404 Not Found 응답으로 처리한다.
public class NotFoundException extends RuntimeException {
    // RuntimeException을 상속하므로 별도의 throws 선언 없이 사용할 수 있다.
}
