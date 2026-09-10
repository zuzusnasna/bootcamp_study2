package com.example.demo.service;

import com.example.demo.dto.ArticleDTO;
import com.example.demo.dto.ArticleForm;
import com.example.demo.model.Article;
import com.example.demo.model.Member;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableArgumentResolver;
import org.springframework.stereotype.Service;

// 게시글과 관련된 비즈니스 로직을 담당하는 Service
@Service
// final 필드인 Repository들을 생성자로 주입받을 수 있도록 생성자를 자동으로 만든다.
@RequiredArgsConstructor
public class ArticleService {

    // 회원 정보 조회가 필요한 경우 사용하는 Repository
    private final MemberRepository memberRepository;

    // 게시글 데이터를 DB에서 조회하거나 저장할 때 사용하는 Repository
    private final ArticleRepository articleRepository;

    // 페이징 관련 요청 정보를 처리하기 위해 주입받은 Resolver
    private final PageableArgumentResolver pageableArgumentResolver;

    // DB에서 사용하는 Article Entity를 화면에 전달할 ArticleDTO로 변환하는 메서드
    private ArticleDTO mapToArticleDTO(Article article){
        // ArticleDTO의 Builder를 이용하여 필요한 게시글 정보를 하나씩 담는다.
        return ArticleDTO.builder()
                // 게시글의 고유 ID를 DTO에 저장한다.
                .id(article.getId())
                // Article과 연결된 회원의 이름을 작성자 이름으로 저장한다.
                .name(article.getMember().getName())
                // Article과 연결된 회원의 이메일을 DTO에 저장한다.
                .email(article.getMember().getEmail())
                // 게시글을 작성한 회원의 ID를 DTO에 저장한다.
                .memberId(article.getMember().getId())
                // 게시글 제목을 DTO에 저장한다.
                .title(article.getTitle())
                // 게시글 내용을 DTO에 저장한다.
                .description(article.getDescription())
                // 게시글이 처음 작성된 시간을 DTO에 저장한다.
                .created(article.getCreated())
                // 게시글이 마지막으로 수정된 시간을 DTO에 저장한다.
                .updated(article.getUpdated())
                // 지금까지 설정한 값으로 ArticleDTO 객체를 생성한다.
                .build();
    }

    // 페이징 조건에 맞는 게시글을 조회하여 화면에서 사용할 Page<ArticleDTO>로 반환한다.
    public Page<ArticleDTO> findAll(Pageable pageable){
        // 전달받은 Pageable을 Repository에 넘겨 현재 페이지에 필요한 게시글만 조회한다.
        return articleRepository.findAll(pageable)
                // 조회된 Article Entity 각각을 ArticleDTO로 변환한다.
                // Page의 map()을 사용하기 때문에 페이징 정보는 유지하면서 내용만 DTO로 변경된다.
                .map(this::mapToArticleDTO);
    }

    // 게시글 ID를 기준으로 특정 게시글 하나를 조회한다.
    public ArticleDTO findById(Long id){
        // Repository에서 전달받은 ID와 일치하는 게시글을 조회한다.
        return articleRepository.findById(id)
                // 조회된 Article Entity를 화면에서 사용할 ArticleDTO로 변환한다.
                .map(this::mapToArticleDTO)
                // 해당 ID의 게시글이 존재하지 않으면 예외를 발생시킨다.
                .orElseThrow();
    }

    public ArticleDTO create(
            Long memberId,
            ArticleForm articleForm){
        Member member = memberRepository.findById(memberId)
                .orElseThrow();

        Article article = Article.builder()
                .title(articleForm.getTitle())
                .description(articleForm.getDescription())
                .member(member)
                .build();

        articleRepository.save(article);
        return mapToArticleDTO(article);
    }
}
