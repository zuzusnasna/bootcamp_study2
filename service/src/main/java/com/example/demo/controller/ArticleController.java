package com.example.demo.controller;

import com.example.demo.dto.ArticleResponse;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/api/articles")
    public List<ArticleResponse> getByMember(
            @RequestParam(name = "memberId", required = false) Long memberId) {
        if (memberId == null){
            return articleService.findAll();
        }else{
            return articleService.findByMemberId(memberId);
        }
    }

    @GetMapping("/{id}")
    public ArticleResponse get(@PathVariable("id") Long id){
        return articleService.findById(id);
    }
}
