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

    // 로그인한 회원의 ID와 작성 폼을 이용하여 새로운 게시글을 생성한다.
    public ArticleDTO create(
            // 게시글 작성자로 연결할 회원의 ID
            Long memberId,
            // 사용자가 입력한 게시글 제목과 내용을 담은 폼 객체
            ArticleForm articleForm){

        // 회원 ID로 실제 회원 Entity를 조회한다.
        // 게시글의 작성자(Member)와 연결하기 위해 Entity가 필요하다.
        Member member = memberRepository.findById(memberId)
                // 해당 ID의 회원이 존재하지 않으면 예외를 발생시킨다.
                .orElseThrow();

        // Article Builder를 사용하여 새 게시글 Entity를 만든다.
        Article article = Article.builder()
                // 작성 폼에서 입력받은 제목을 게시글에 저장한다.
                .title(articleForm.getTitle())
                // 작성 폼에서 입력받은 내용을 게시글에 저장한다.
                .description(articleForm.getDescription())
                // 조회한 회원 Entity를 게시글 작성자로 연결한다.
                .member(member)
                // Builder 설정을 끝내고 Article Entity를 생성한다.
                .build();

        // 완성된 Article Entity를 Repository를 통해 DB에 저장한다.
        articleRepository.save(article);

        // 저장한 Article Entity를 화면에서 사용할 ArticleDTO로 변환하여 반환한다.
        return mapToArticleDTO(article);
    }

    // ArticleForm에 담긴 게시글 ID를 기준으로 기존 게시글을 수정한다.
    public ArticleDTO update(ArticleForm articleForm) {

        // 수정할 게시글 ID로 기존 Article Entity를 조회한다.
        Article article = articleRepository
                .findById(articleForm.getId())
                // 해당 ID의 게시글이 존재하지 않으면 예외를 발생시킨다.
                .orElseThrow();

        // 수정 폼에서 전달받은 제목으로 기존 게시글 제목을 변경한다.
        article.setTitle(articleForm.getTitle());
        // 수정 폼에서 전달받은 내용으로 기존 게시글 내용을 변경한다.
        article.setDescription(articleForm.getDescription());

        // 변경된 Article Entity를 Repository를 통해 저장한다.
        articleRepository.save(article);

        // 수정된 게시글을 ArticleDTO로 변환하여 반환한다.
        return mapToArticleDTO(article);
    }

    // 게시글 ID를 기준으로 게시글을 실제 DB에서 삭제한다.
    public void delete(Long id){
        // 존재하지 않는 게시글을 삭제하려는 경우 예외를 발생시켜 잘못된 요청을 처리한다.
        if (!articleRepository.existsById(id)) {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }

        // JpaRepository가 제공하는 deleteById()를 사용하여 해당 게시글을 삭제한다.
        articleRepository.deleteById(id);
    }
}
