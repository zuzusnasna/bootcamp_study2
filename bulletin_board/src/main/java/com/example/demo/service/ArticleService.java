package com.example.demo.service;

import com.example.demo.dto.ArticleDTO;
import com.example.demo.model.Article;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// 게시글과 관련된 비즈니스 로직을 담당하는 Service
@Service
@RequiredArgsConstructor
public class ArticleService {

    // 회원 정보 조회를 위한 Repository
    private final MemberRepository memberRepository;

    // 게시글 DB 작업을 위한 Repository
    private final ArticleRepository articleRepository;

    // Entity인 Article을 화면 전달용 ArticleDTO로 변환
    private ArticleDTO mapToArticleDTO(Article article){
        return ArticleDTO.builder()
                // 게시글 ID
                .id(article.getId())
                // 작성자 이름
                .name(article.getMember().getName())
                // 작성자 이메일
                .email(article.getMember().getEmail())
                // 작성자 회원 ID
                .memberId(article.getMember().getId())
                // 게시글 제목
                .title(article.getTitle())
                // 게시글 내용
                .description(article.getDescription())
                // 작성 시간
                .created(article.getCreated())
                // 수정 시간
                .updated(article.getUpdated())
                .build();
    }
}
