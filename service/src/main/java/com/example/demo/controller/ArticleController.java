package com.example.demo.controller;

import com.example.demo.dto.ArticleResponse;
import com.example.demo.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    // 전체 게시글 조회
    // GET /api/articles
    // GET /api/articles?memberId=1
    @GetMapping
    public List<ArticleResponse> getByMember(
            @RequestParam(name = "memberId", required = false) Long memberId) {

        if (memberId == null) {
            return articleService.findAll();
        } else {
            return articleService.findByMemberId(memberId);
        }
    }

    // 게시글 하나 조회
    // GET /api/articles/1
    @GetMapping("/{id}")
    public ArticleResponse get(@PathVariable("id") Long id) {
        return articleService.findById(id);
    }
}