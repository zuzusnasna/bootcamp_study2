package com.example.demo.service;

// 화면에 게시글 데이터를 전달하기 위한 DTO
import com.example.demo.dto.ArticleDTO;
// DB에서 조회한 게시글 Entity
import com.example.demo.model.Article;
// 게시글 DB 조회/저장을 담당하는 Repository
import com.example.demo.repository.ArticleRepository;
// 회원 DB 조회를 담당하는 Repository
import com.example.demo.repository.MemberRepository;
// final 필드를 생성자 주입받을 수 있도록 생성자를 자동 생성하는 Lombok 애너테이션
import lombok.RequiredArgsConstructor;
// 이 클래스를 Spring의 Service Bean으로 등록하는 애너테이션
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableArgumentResolver;
import org.springframework.stereotype.Service;

// 여러 게시글을 목록으로 반환하기 위해 List를 사용한다.


// 게시글과 관련된 비즈니스 로직을 담당하는 Service
@Service
// final 필드인 Repository들을 생성자로 주입받을 수 있도록 생성자를 자동으로 만든다.
@RequiredArgsConstructor
public class ArticleService {

    // 회원 정보 조회가 필요한 경우 사용하는 Repository
    private final MemberRepository memberRepository;

    // 게시글 데이터를 DB에서 조회하거나 저장할 때 사용하는 Repository
    private final ArticleRepository articleRepository;
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

    // DB에 저장된 모든 게시글을 조회하여 화면에서 사용할 DTO 목록으로 반환한다.
    public Page<ArticleDTO> findAll(Pageable pageable){
        // ArticleRepository를 통해 DB의 모든 Article Entity를 조회한다.
        return articleRepository.findAll(pageable)
                // 각 Article Entity를 위에서 만든 mapToArticleDTO()를 이용해 DTO로 변환한다.
                .map(this::mapToArticleDTO);


    }
}
