package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Thymeleaf 화면에 필요한 데이터를 전달하는 Controller.
 *
 * Controller에서 Model에 데이터를 담으면
 * Thymeleaf Template에서 해당 값을 사용하여 HTML을 동적으로 렌더링한다.
 */
@Controller // View를 반환하는 Spring MVC Controller로 등록한다.
public class BasicController {

    /**
     * 책 정보를 Thymeleaf 템플릿으로 전달한다.
     *
     * GET /book 요청이 들어오면 Model에 데이터를 추가한 뒤
     * /basic/book 템플릿을 View로 반환한다.
     */
    @GetMapping("/book")
    public String getBook(Model model) {
        // Thymeleaf에서 ${title}로 사용할 제목 데이터를 Model에 저장한다.
        model.addAttribute("title", "이것이 스프링 부트이다.");

        // Thymeleaf에서 ${description}으로 사용할 설명 데이터를 Model에 저장한다.
        model.addAttribute("description", "예제를 통해 공부해 보세요");

        // templates/basic/book.html을 View로 렌더링한다.
        return "/basic/book";
    }
}
