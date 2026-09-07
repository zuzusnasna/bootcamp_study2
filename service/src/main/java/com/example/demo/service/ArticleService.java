package com.example.demo.service;

import com.example.demo.dto.ArticleRequest;
import com.example.demo.dto.ArticleResponse;
import com.example.demo.entity.Article;
import com.example.demo.entity.Member;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 게시글 관련 비즈니스 로직을 담당하는 Service.
 *
 * 전체 흐름:
 * Controller -> ArticleService -> Repository -> DB
 *                  ↓
 *          Entity 생성/조회/수정
 *                  ↓
 *            Response DTO 변환
 *                  ↓
 *              Controller
 */
@Service
@RequiredArgsConstructor // final Repository들을 생성자로 주입
public class ArticleService {

    // 게시글 작성자를 확인하기 위해 MemberRepository도 사용한다.
    private final MemberRepository memberRepository;

    // Article Entity의 DB 접근을 담당한다.
    private final ArticleRepository articleRepository;

    /**
     * 특정 회원의 게시글을 생성한다.
     *
     * 처리 순서:
     * 1. memberId로 작성자 Member 조회
     * 2. 회원이 없으면 NotFoundException
     * 3. ArticleRequest -> Article Entity 변환
     * 4. Article에 작성자 Member 연결
     * 5. Repository.save()로 저장
     * 6. ArticleResponse로 변환
     */
    public ArticleResponse create(Long memberId, ArticleRequest articleRequest) {
        // 게시글은 반드시 존재하는 회원이 작성해야 하므로 먼저 회원을 조회한다.
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundException::new);

        // Request DTO의 데이터를 Article Entity에 옮긴다.
        Article article = Article.builder()
                .title(articleRequest.getTitle())
                .description(articleRequest.getDescription())
                // Article N : 1 Member 관계에서 작성자를 연결한다.
                .member(member)
                .build();

        // Article Entity를 DB에 저장한다.
        articleRepository.save(article);

        // 저장된 Entity를 외부 응답용 DTO로 변환한다.
        return mapToArticleResponse(article);
    }

    /**
     * Article Entity를 ArticleResponse DTO로 변환한다.
     *
     * Entity의 member 관계를 따라가서 작성자의 id/name/email도
     * Response DTO에 함께 넣는다.
     */
    private ArticleResponse mapToArticleResponse(Article article) {
        return ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .description(article.getDescription())
                .created(article.getCreated())
                .updated(article.getUpdated())
                .memberId(article.getMember().getId())
                .name(article.getMember().getName())
                .email(article.getMember().getEmail())
                .build();
    }

    /**
     * 전체 게시글을 조회한다.
     *
     * Repository.findAll() -> List<Article>
     * -> stream으로 순회
     * -> 각 Article을 ArticleResponse로 변환
     * -> List로 반환
     */
    public List<ArticleResponse> findAll() {
        return articleRepository.findAll()
                .stream()
                .map(this::mapToArticleResponse)
                .toList();
    }

    /**
     * 특정 회원이 작성한 게시글 목록을 조회한다.
     *
     * 1. memberId로 Member를 조회한다.
     * 2. Member가 없으면 404를 발생시킨다.
     * 3. ArticleRepository.findByMember()로 해당 회원의 게시글을 조회한다.
     */
    public List<ArticleResponse> findByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundException::new);

        return articleRepository.findByMember(member)
                .stream()
                .map(this::mapToArticleResponse)
                .toList();
    }

    /**
     * 게시글 하나를 ID로 조회한다.
     *
     * findById()의 결과가 없으면 NotFoundException이 발생하고
     * @ResponseStatus 설정에 따라 HTTP 404 응답이 된다.
     */
    public ArticleResponse findById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        return mapToArticleResponse(article);
    }

    /**
     * 게시글을 수정한다.
     *
     * 처리 순서:
     * 1. id로 기존 Article 조회
     * 2. 존재하지 않으면 예외 발생
     * 3. 제목/본문 변경
     * 4. Repository.save()로 저장
     * 5. Response DTO로 변환
     *
     * Article의 updated 필드는 JPA Auditing을 통해 수정 시간을 기록한다.
     */
    public ArticleResponse update(Long id, ArticleRequest articleRequest) {
        Article article = articleRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        // 기존 Entity에 Request DTO의 새로운 값을 적용한다.
        article.setTitle(articleRequest.getTitle());
        article.setDescription(articleRequest.getDescription());

        // 변경된 Article을 DB에 저장한다.
        articleRepository.save(article);

        return mapToArticleResponse(article);
    }
}
