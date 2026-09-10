package com.example.demo.controller;

// 게시글 데이터를 화면에 전달하기 위해 사용하는 DTO
import com.example.demo.dto.ArticleDTO;
// 게시글 관련 비즈니스 로직을 처리하는 Service
import com.example.demo.service.ArticleService;
// final 필드를 생성자 주입받을 수 있도록 생성자를 자동 생성하는 Lombok 애너테이션
import lombok.RequiredArgsConstructor;
// 현재 클래스에서 로그를 사용할 수 있도록 Logger를 자동 생성하는 Lombok 애너테이션
import lombok.extern.slf4j.Slf4j;
// 해당 클래스를 Spring MVC Controller로 등록하는 애너테이션
import org.springframework.stereotype.Controller;
// Controller에서 화면으로 데이터를 전달할 때 사용하는 객체
import org.springframework.ui.Model;
// GET 방식의 HTTP 요청을 특정 메서드와 연결하는 애너테이션
import org.springframework.web.bind.annotation.GetMapping;
// Controller 내부의 공통 URL을 지정하는 애너테이션
import org.springframework.web.bind.annotation.RequestMapping;

// 여러 개의 게시글을 목록으로 다루기 위해 List를 사용한다.
import java.util.List;

// 이 클래스를 Spring MVC의 Controller로 등록한다.
@Controller
// 이 Controller의 모든 요청 URL 앞에 '/article'을 붙인다.
@RequestMapping("/article")
// final 필드인 ArticleService를 생성자로 주입받을 수 있도록 생성자를 자동으로 만든다.
@RequiredArgsConstructor
// log 객체를 자동으로 만들어 주어 로그를 사용할 수 있게 한다.
@Slf4j
public class ArticleController {

    // 게시글 조회와 같은 비즈니스 로직을 ArticleService에 맡긴다.
    private final ArticleService articleService;

    // '/article/list'로 들어오는 GET 요청을 처리한다.
    @GetMapping("/list")
    public String getArticleList(Model model){

        // Service에서 DB의 게시글을 조회하고 화면에 전달할 DTO 목록으로 받아온다.
        List<ArticleDTO> articles = articleService.findAll();

        // 조회한 게시글 목록을 'articles'라는 이름으로 Model에 저장한다.
        // View에서는 이 이름을 사용하여 게시글 목록을 화면에 출력할 수 있다.
        model.addAttribute("articles", articles);

        // Thymeleaf가 article-list.html 화면을 렌더링하도록 View 이름을 반환한다.
        return "article-list";
    }
}
