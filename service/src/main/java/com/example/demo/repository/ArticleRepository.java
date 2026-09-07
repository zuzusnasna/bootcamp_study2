package com.example.demo.repository;

import com.example.demo.entity.Article;
import com.example.demo.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Article Entity의 DB 접근을 담당하는 Repository.
 *
 * 흐름:
 * ArticleService -> ArticleRepository -> JPA/Hibernate -> DB
 */
@Repository // Repository 계층의 Bean으로 등록
public interface ArticleRepository extends JpaRepository<Article, Long> {

    /**
     * 특정 회원이 작성한 게시글 목록을 조회한다.
     *
     * 메서드 이름을 기준으로 Spring Data JPA가 쿼리를 자동으로 만든다.
     * findBy + Member -> Article의 member 필드를 기준으로 검색
     *
     * 즉, 직접 SQL을 작성하지 않고도
     * "이 Member가 작성한 Article을 찾아줘"라는 조회가 가능하다.
     */
    List<Article> findByMember(Member member);
}
