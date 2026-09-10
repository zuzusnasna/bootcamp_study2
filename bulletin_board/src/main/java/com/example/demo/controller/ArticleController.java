package com.example.demo.controller;

import com.example.demo.dto.ArticleDTO;
import com.example.demo.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/article")
@RequiredArgsConstructor
@Slf4j
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/list")
    public String getArticleList(Model model){

        List<ArticleDTO> articles = articleService.findAll();

        model.addAttribute("articles", articles);
        return "article-list";
    }
}
