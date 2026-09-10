package com.example.demo.controller;

import com.example.demo.dto.ArticleDTO;
import com.example.demo.dto.ArticleForm;
import com.example.demo.model.MemberUserDetails;
import com.example.demo.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

// 게시글 목록과 상세 보기 화면으로 요청을 전달하는 Controller
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
            Model model) {

        // Service에 페이지 조회 조건을 전달하고 게시글 페이지를 조회한다.
        Page<ArticleDTO> page = articleService.findAll(pageable);

        // 조회한 페이지 정보를 'page'라는 이름으로 View에 전달한다.
        model.addAttribute("page", page);

        // Thymeleaf가 article-list.html 화면을 렌더링하도록 View 이름을 반환한다.
        return "article-list";
    }

    // '/article/content?id=게시글ID'로 들어오는 GET 요청을 처리한다.
    @GetMapping("/content")
    public String getArticle(
            // 요청 URL의 id 파라미터를 게시글 ID로 전달받는다.
            @RequestParam("id") Long id,
            Model model) {

        // 게시글 ID를 Service에 전달하여 해당 게시글을 조회한다.
        // 조회한 ArticleDTO를 'article'이라는 이름으로 View에 전달한다.
        model.addAttribute(
                "article",
                articleService.findById(id)
        );

        // Thymeleaf가 article-content.html 화면을 렌더링하도록 View 이름을 반환한다.
        return "article-content";
    }

    // '/article/add'로 들어오는 GET 요청을 처리하여 게시글 작성 화면을 보여준다.
    @GetMapping("/add")
    public String getArticleAdd(
            // 폼 입력값을 ArticleForm 객체에 담고 View에서는 'article'이라는 이름으로 사용한다.
            @ModelAttribute("article") ArticleForm articleForm) {

        // Thymeleaf가 게시글 작성 화면인 article-add.html을 렌더링하도록 View 이름을 반환한다.
        return "article-add";
    }

    // '/article/add'로 들어오는 POST 요청을 처리하여 게시글을 등록한다.
    @PostMapping("/add")
    public String postArticleAdd(
            // 사용자가 입력한 제목과 내용을 ArticleForm에 담아서 전달받는다.
            @ModelAttribute("article") ArticleForm articleForm,
            // Spring Security가 인증한 현재 사용자의 정보를 주입받는다.
            @AuthenticationPrincipal MemberUserDetails userDetails) {

        // 로그인한 회원의 ID와 작성 폼을 Service에 전달하여 게시글을 생성한다.
        articleService.create(
                userDetails.getMemberId(),
                articleForm
        );

        // 게시글 작성이 끝나면 게시글 목록으로 이동한다.
        return "redirect:/article/list";
    }

    @PostMapping("/add")
    public String postArticleAdd(
            @Valid @ModelAttribute("article") ArticleForm articleForm,
            BindingResult bindingResult,
            @AuthenticationPrincipal MemberUserDetails userDetails) {
        if (articleForm.getTitle() != null &&
                articleForm.getTitle().contains("ㅆㅃ")) {

            bindingResult.rejectValue(
                    "title",
                    "SlangDetected",
                    "욕하면 안되지"
            );
        }

        if (articleForm.getDescription() != null &&
                articleForm.getDescription().contains("ㅆㅃ")) {

            bindingResult.rejectValue(
                    "description",
                    "SlangDetected",
                    "욕하면 안되지"
            );
        }

        if (bindingResult.hasErrors()) {
            return "article-add";
        }

        articleService.create(
                userDetails.getMemberId(),
                articleForm
        );
        return "redirect:/article/list";
    }
}
