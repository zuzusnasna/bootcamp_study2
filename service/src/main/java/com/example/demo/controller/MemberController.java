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

/**
 * 회원 관련 HTTP 요청을 가장 먼저 받는 Controller 계층.
 *
 * 전체적인 흐름은 다음과 같다.
 * Client -> Controller -> Service -> Repository -> DB
 *                         ↓
 *                    Entity/DTO 변환
 *                         ↓
 * Client <- JSON Response <- Controller
 *
 * Controller에서는 DB 작업이나 핵심 비즈니스 로직을 직접 처리하지 않고
 * 요청 데이터를 Service에 전달하고 Service의 결과를 HTTP 응답으로 반환한다.
 */
@RestController // 일반적인 Controller와 달리 반환값을 JSON 형태의 HTTP 응답 본문으로 전달
@RequestMapping("/api/members") // 이 Controller의 모든 URL 앞에 /api/members가 붙는다.
@RequiredArgsConstructor // final 필드를 매개변수로 받는 생성자를 Lombok이 자동 생성
public class MemberController {

    // Controller가 직접 Repository를 호출하지 않고 Service를 통해 비즈니스 로직을 위임한다.
    private final MemberService memberService;
    private final ArticleService articleService;

    /**
     * 여러 회원을 한 번에 등록한다.
     * POST /api/members
     *
     * @RequestBody가 HTTP 요청 JSON을 MemberRequest 객체 목록으로 변환한다.
     * 이후 실제 회원 생성 작업은 MemberService가 담당한다.
     */
    @PostMapping
    public List<MemberResponse> post(
            @RequestBody List<MemberRequest> memberRequests) {
        // Controller -> MemberService -> MemberRepository -> DB 순서로 처리된다.
        // Service는 저장된 Entity를 클라이언트용 MemberResponse DTO로 변환해서 반환한다.
        return memberService.createBatch(memberRequests);
    }

    /**
     * 특정 회원이 게시글을 작성한다.
     * POST /api/members/{id}/articles
     *
     * URL의 {id}는 게시글 작성자의 회원 ID이고,
     * 요청 본문의 JSON은 ArticleRequest로 전달된다.
     */
    @PostMapping("/{id}/articles")
    @ResponseStatus(HttpStatus.CREATED) // 정상적으로 생성되었을 때 HTTP 201 Created 반환
    public ArticleResponse postArticle(
            @PathVariable("id") Long id, // URL 경로의 회원 ID를 가져온다.
            @RequestBody ArticleRequest articleRequest) { // JSON 게시글 데이터를 DTO로 변환
        // 게시글 생성에 필요한 회원 조회와 Entity 생성/저장은 ArticleService가 담당한다.
        return articleService.create(id, articleRequest);
    }

    /**
     * 전체 회원을 조회한다.
     * GET /api/members
     */
    @GetMapping
    public List<MemberResponse> getAll() {
        // Service -> Repository -> DB에서 회원을 조회한 뒤 Response DTO 목록으로 반환한다.
        return memberService.findAll();
    }

    /**
     * 특정 회원이 작성한 게시글을 조회한다.
     * GET /api/members/{id}/articles
     *
     * 현재 코드는 ArticleController의 API로 요청을 forward한다.
     * 따라서 실제 게시글 조회 로직은 ArticleController -> ArticleService에서 처리된다.
     */
    @GetMapping("{id}/articles")
    public void getArticle(
            @PathVariable("id") Long id,
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // /api/articles?memberId={id} 요청으로 내부 forward한다.
        // 브라우저가 별도의 요청을 보내는 것이 아니라 서버 내부에서 다음 Controller로 넘긴다.
        request.getSession()
                .getServletContext()
                .getRequestDispatcher("/api/articles?memberId=" + id)
                .forward(request, response);
    }

    /**
     * 회원 정보를 수정한다.
     * PUT /api/members/{id}
     */
    @PutMapping("/{id}")
    public MemberResponse updateMember(
            @PathVariable Long id, // 수정할 회원의 PK
            @RequestBody MemberRequest memberRequest) { // 수정할 회원 정보를 JSON에서 DTO로 변환
        // 실제 조회 -> 값 변경 -> 저장 과정은 MemberService가 담당한다.
        return memberService.update(id, memberRequest);
    }

    /**
     * 회원을 삭제한다.
     * DELETE /api/members/{id}
     */
    @DeleteMapping("/{id}")
    public void deleteMember(@PathVariable Long id) {
        // Service에서 회원 존재 여부를 확인한 후 Repository를 통해 삭제한다.
        memberService.delete(id);
    }
}