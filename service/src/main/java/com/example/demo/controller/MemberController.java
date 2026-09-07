package com.example.demo.controller;

import com.example.demo.dto.ArticleRequest;
import com.example.demo.dto.ArticleResponse;
import com.example.demo.dto.MemberRequest;
import com.example.demo.dto.MemberResponse;
import com.example.demo.service.ArticleService;
import com.example.demo.service.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController // 진입지점을 명시
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final ArticleService articleService;

    // 회원 입력
    @PostMapping
    public List<MemberResponse> post(
            @RequestBody List<MemberRequest> memberRequests) {
        return memberService.createBatch(memberRequests);
    }

    // 특정 회원의 게시글 작성
    @PostMapping("/{id}/articles")
    @ResponseStatus(HttpStatus.CREATED)
    public ArticleResponse postArticle(
            @PathVariable("id") Long id,
            @RequestBody ArticleRequest articleRequest) {
        return articleService.create(id, articleRequest);
    }

    @GetMapping
    public List<MemberResponse> getAll() {
        return memberService.findAll();
    }

    // 특정 회원의 게시글 조회
    @GetMapping("{id}/articles")
    public void getArticle(
            @PathVariable("id") Long id,
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getSession()
                .getServletContext()
                .getRequestDispatcher("/api/articles?memberId=" + id)
                .forward(request, response);
    }

    // 회원 수정
    @PutMapping("/{id}")
    public MemberResponse updateMember(
            @PathVariable Long id,
            @RequestBody MemberRequest memberRequest) {
        return memberService.update(id, memberRequest);
    }

    // 회원 삭제
    @DeleteMapping("/{id}")
    public void deleteMember(@PathVariable Long id) {
        memberService.delete(id);
    }
}