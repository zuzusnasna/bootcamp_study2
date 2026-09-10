package com.example.demo.controller;

import com.example.demo.dto.ArticleDTO;
import com.example.demo.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

// 게시글 목록 화면으로 요청을 전달하는 Controller
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
    public String getArticleList(
            // 페이지당 게시글 수, 정렬 기준과 방향을 기본값으로 설정한다.
            @PageableDefault(
                    size = 10,
                    sort = "id",
                    direction = Sort.Direction.DESC)
            // 요청 URL의 page, size 등의 정보를 바탕으로 페이지 조회 조건을 전달받는다.
            Pageable pageable,
            // 조회한 게시글 페이지 정보를 View에 전달하기 위한 객체
            Model model){

        // Service에 페이지 조회 조건을 전달하고 게시글 페이지를 조회한다.
        Page<ArticleDTO> page = articleService.findAll(pageable);

        // 조회한 페이지 정보를 'page'라는 이름으로 View에 전달한다.
        model.addAttribute("page", page);

        // Thymeleaf가 article-list.html 화면을 렌더링하도록 View 이름을 반환한다.
        return "article-list";
    }

    @GetMapping("/content")
    public String getArticle(
            @RequestParam("id") Long id,
            Model model){
        model.addAttribute(
                "article",
                articleService.findById(id)
        );
        return "article-content";
    }
    )
}
