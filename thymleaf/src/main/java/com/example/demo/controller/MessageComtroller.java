package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 메시지 표현식 예제를 처리하는 Controller.
 *
 * messages.properties에 정의된 메시지를 Thymeleaf 템플릿에서
 * #{...} 문법으로 사용할 수 있도록 View를 연결한다.
 */
@Controller // View 이름을 반환하는 Spring MVC Controller로 등록한다.
public class MessageComtroller {

    /**
     * Thymeleaf 메시지 표현식 예제 화면을 반환한다.
     *
     * GET /message/basic
     * 요청이 들어오면 templates/message/message-basic.html을 렌더링한다.
     */
    @GetMapping("/message/basic")
    public String getMessageBasic() {
        // Thymeleaf가 templates 아래의 message/message-basic.html을 찾아 렌더링한다.
        return "message/message-basic";
    }
}
