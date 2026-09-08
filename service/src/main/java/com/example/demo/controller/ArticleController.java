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
 * 전체 요청 흐름:
 * Client
 *   ↓ HTTP 요청
 * ArticleController
 *   ↓ 필요한 데이터만 Service에 전달
 * ArticleService
 *   ↓ Repository 호출
 * ArticleRepository
 *   ↓
 * Database
 *   ↓ Entity
 * ArticleService
 *   ↓ Response DTO
 * ArticleController
 *   ↓ JSON 응답
 * Client
 *
 * Controller의 핵심 역할은 HTTP 요청을 해석하고 Service를 호출하는 것이다.
 * 실제 게시글 생성/조회/수정/삭제와 같은 비즈니스 로직은 Service 계층이 담당한다.
 */
@RestController
@RequestMapping("/api/articles") // 게시글 API의 공통 URL. 모든 메서드 앞에 /api/articles가 붙는다.
@RequiredArgsConstructor // final 필드인 articleService를 생성자를 통해 자동 주입한다.
public class ArticleController {

    // 게시글 관련 비즈니스 로직을 담당하는 Service.
    private final ArticleService articleService;

    /**
     * 게시글 목록을 조회한다.
     *
     * GET /api/articles
     *   → 전체 게시글 조회
     *
     * GET /api/articles?memberId=1
     *   → memberId가 1인 회원의 게시글만 조회
     *
     * @RequestParam:
     * URL의 Query String 값을 메서드 파라미터로 받아온다.
     * required = false이므로 memberId를 전달하지 않아도 된다.
     */
    @GetMapping
    public List<ArticleResponse> getByMember(
            @RequestParam(name = "memberId", required = false) Long memberId) {

        // memberId가 없으면 전체 게시글을 조회한다.
        if (memberId == null) {
            return articleService.findAll();
        }

        // memberId가 있으면 해당 회원이 작성한 게시글만 조회한다.
        return articleService.findByMemberId(memberId);
    }

    /**
     * 게시글 하나를 ID로 조회한다.
     *
     * GET /api/articles/1
     *
     * @PathVariable:
     * URL 경로에 포함된 {id} 값을 Long id로 받아온다.
     *
     * Service가 반환한 ArticleResponse는 Spring이 자동으로 JSON 형태로 변환하여 응답한다.
     */
    @GetMapping("/{id}")
    public ArticleResponse get(@PathVariable("id") Long id) {
        return articleService.findById(id);
    }

    /**
     * 게시글을 수정한다.
     *
     * PUT /api/articles/{id}
     *
     * 요청 처리 과정:
     * 1. URL의 id를 @PathVariable로 받는다.
     * 2. 요청 JSON을 @RequestBody로 ArticleRequest에 바인딩한다.
     * 3. id와 Request DTO를 Service에 전달한다.
     * 4. Service에서 기존 Entity를 수정하고 DB에 반영한다.
     * 5. 수정된 Entity를 ArticleResponse로 변환하여 반환한다.
     */
    @PutMapping("/{id}")
    public ArticleResponse put(
            @PathVariable("id") Long id,
            @RequestBody ArticleRequest articleRequest) {
        return articleService.update(id, articleRequest);
    }

    /**
     * 게시글을 삭제한다.
     *
     * DELETE /api/articles/{id}
     *
     * 처리 흐름:
     * Client
     *   → DELETE 요청
     *   → Controller가 id 추출
     *   → ArticleService.delete(id)
     *   → Repository에서 Article 조회
     *   → 존재하면 삭제
     *   → 존재하지 않으면 NotFoundException
     *
     * 삭제처럼 별도의 응답 데이터가 필요하지 않기 때문에 void를 반환한다.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        // URL에서 전달받은 게시글 ID를 Service에 넘겨 실제 삭제 작업을 위임한다.
        articleService.delete(id);
    }
}
