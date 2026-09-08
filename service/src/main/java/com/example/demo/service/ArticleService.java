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
 * 게시글 관련 비즈니스 로직을 담당하는 Service 계층.
 *
 * 전체 흐름:
 * Client → Controller → ArticleService → Repository → DB
 *                                      ↓
 *                              Entity 생성/조회/수정
 *                                      ↓
 *                              Response DTO 변환
 *                                      ↓
 *                                   Controller
 *                                      ↓
 *                                    Client
 *
 * Controller는 HTTP 요청을 받아 Service를 호출하고,
 * Service는 실제 업무 규칙과 데이터 처리 과정을 담당한다.
 */
@Service // Spring이 이 클래스를 Service Bean으로 등록한다.
@RequiredArgsConstructor // final 필드를 생성자 주입 방식으로 자동 연결한다.
public class ArticleService {

    // 게시글 작성자가 실제 존재하는 회원인지 확인하기 위해 사용한다.
    private final MemberRepository memberRepository;

    // Article Entity에 대한 DB 저장/조회/수정/삭제를 담당한다.
    private final ArticleRepository articleRepository;

    /**
     * 특정 회원의 게시글을 생성한다.
     *
     * 처리 순서:
     * 1. memberId로 작성자 회원 조회
     * 2. 회원이 존재하지 않으면 예외 발생
     * 3. ArticleRequest를 Article Entity로 변환
     * 4. Article과 Member의 관계 연결
     * 5. Repository.save()로 DB 저장
     * 6. 저장된 Entity를 Response DTO로 변환
     */
    public ArticleResponse create(Long memberId, ArticleRequest articleRequest) {
        // 게시글을 작성할 회원이 실제로 존재하는지 먼저 확인한다.
        Member member = memberRepository.findById(memberId)
                // Optional에 값이 없으면 NotFoundException을 발생시킨다.
                .orElseThrow(NotFoundException::new);

        // 클라이언트가 보낸 Request DTO의 값을 DB 저장용 Entity로 옮긴다.
        Article article = Article.builder()
                .title(articleRequest.getTitle())
                .description(articleRequest.getDescription())
                // Article N : 1 Member 관계에서 게시글 작성자를 연결한다.
                .member(member)
                .build();

        // Entity를 Repository에 전달하면 JPA/Hibernate가 DB INSERT를 수행한다.
        articleRepository.save(article);

        // 외부 응답에서는 Entity 대신 Response DTO를 반환한다.
        return mapToArticleResponse(article);
    }

    /**
     * Article Entity를 ArticleResponse DTO로 변환한다.
     *
     * Entity를 직접 Controller까지 노출하지 않고 DTO로 변환하는 이유:
     * - DB 구조와 API 응답 구조를 분리할 수 있다.
     * - Entity의 불필요한 필드를 외부에 노출하지 않을 수 있다.
     * - API 응답 형식을 독립적으로 관리할 수 있다.
     *
     * 또한 Article의 member 관계를 따라가 작성자 정보까지 응답에 담는다.
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
     * 모든 게시글을 조회한다.
     *
     * Repository.findAll()로 Entity 목록을 가져온 뒤
     * Stream의 map()을 이용해 각각의 Article을 ArticleResponse로 변환한다.
     */
    public List<ArticleResponse> findAll() {
        return articleRepository.findAll()
                .stream()
                // 메서드 참조를 이용해 각 Article을 Response DTO로 변환한다.
                .map(this::mapToArticleResponse)
                // 변환된 Stream을 다시 List로 만든다.
                .toList();
    }

    /**
     * 특정 회원이 작성한 게시글을 조회한다.
     *
     * memberId 자체가 존재하는지도 먼저 확인한 후,
     * Repository의 findByMember()를 호출하여 해당 회원의 게시글만 조회한다.
     */
    public List<ArticleResponse> findByMemberId(Long memberId) {
        // 먼저 회원이 존재하는지 검사한다.
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundException::new);

        // 존재하는 Member를 기준으로 게시글을 조회하고 Response DTO로 변환한다.
        return articleRepository.findByMember(member)
                .stream()
                .map(this::mapToArticleResponse)
                .toList();
    }

    /**
     * 게시글 하나를 ID로 조회한다.
     *
     * findById()는 Optional<Article>을 반환한다.
     * 게시글이 존재하면 Entity를 꺼내고,
     * 존재하지 않으면 NotFoundException을 발생시킨다.
     */
    public ArticleResponse findById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        // 조회한 Entity를 외부 응답용 DTO로 변환한다.
        return mapToArticleResponse(article);
    }

    /**
     * 게시글 정보를 수정한다.
     *
     * 처리 순서:
     * 1. 수정 대상 Article 조회
     * 2. 게시글이 없으면 예외 발생
     * 3. Request DTO의 새로운 값으로 Entity 수정
     * 4. Repository.save()로 DB 반영
     * 5. Response DTO 반환
     *
     * updated 필드는 JPA Auditing에 의해 수정 시간을 자동으로 기록할 수 있다.
     */
    public ArticleResponse update(Long id, ArticleRequest articleRequest) {
        // 기존 게시글을 조회한다.
        Article article = articleRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        // 기존 Entity의 제목과 본문을 새로운 요청 값으로 변경한다.
        article.setTitle(articleRequest.getTitle());
        article.setDescription(articleRequest.getDescription());

        // 변경된 Entity를 저장한다.
        articleRepository.save(article);

        return mapToArticleResponse(article);
    }

    /**
     * 게시글을 삭제한다.
     *
     * 삭제 전에 먼저 ID로 게시글을 조회하는 이유:
     * 존재하지 않는 게시글을 삭제하려는 경우 NotFoundException을 발생시켜
     * 일관된 404 Not Found 응답을 만들기 위해서다.
     */
    public void delete(Long id) {
        // 삭제 대상 Article을 먼저 조회한다.
        Article article = articleRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        // 조회된 Entity를 Repository에 전달하여 DB에서 삭제한다.
        articleRepository.delete(article);
    }
}
