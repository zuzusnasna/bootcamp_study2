package com.example.demo.controller;

import com.example.demo.dto.ArticleRequest;
import com.example.demo.dto.ArticleResponse;
import com.example.demo.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 게시글 관련 HTTP 요청을 처리하는 Controller.
 *
 * 요청 흐름:
 * Client -> ArticleController -> ArticleService -> ArticleRepository -> DB
 *                                      ↓
 *                              Article Entity 처리
 *                                      ↓
 * Client <- ArticleResponse DTO <- Controller
 *
 * Controller의 핵심 역할은 HTTP 요청을 해석하고 Service를 호출하는 것이다.
 * 실제 게시글 조회/수정 같은 작업은 Service에 맡긴다.
 */
@RestController
@RequestMapping("/api/articles") // 게시글 API의 공통 URL
@RequiredArgsConstructor // final로 선언된 articleService를 생성자로 주입
public class ArticleController {

    private final ArticleService articleService;

    /**
     * 게시글 목록 조회.
     *
     * GET /api/articles
     *   -> 전체 게시글 조회
     *
     * GET /api/articles?memberId=1
     *   -> 특정 회원이 작성한 게시글만 조회
     *
     * memberId가 선택값이므로 하나의 API에서 두 가지 조회를 처리한다.
     */
    @GetMapping
    public List<ArticleResponse> getByMember(
            @RequestParam(name = "memberId", required = false) Long memberId) {

        // memberId가 없으면 전체 게시글을 조회한다.
        if (memberId == null) {
            return articleService.findAll();
        }

        // memberId가 있으면 해당 회원의 게시글만 조회한다.
        return articleService.findByMemberId(memberId);
    }

    /**
     * 게시글 하나 조회.
     * GET /api/articles/1
     *
     * URL의 id를 Service에 전달하고,
     * Service가 반환한 ArticleResponse를 JSON으로 응답한다.
     */
    @GetMapping("/{id}")
    public ArticleResponse get(@PathVariable("id") Long id) {
        return articleService.findById(id);
    }

    /**
     * 게시글 수정.
     * PUT /api/articles/{id}
     *
     * 요청 JSON -> ArticleRequest DTO
     * PathVariable id + DTO -> ArticleService
     * Service에서 Entity 수정 후 ArticleResponse로 변환
     */
    @PutMapping("/{id}")
    public ArticleResponse put(
            @PathVariable("id") Long id,
            @RequestBody ArticleRequest articleRequest) {
        return articleService.update(id, articleRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id")Long id){
        articleService.delete(id);
    }
}