package com.example.demo.repository;

import com.example.demo.model.Article;
import com.example.demo.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

// Article Entity의 DB 작업을 담당하는 Repository이다.
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    // 특정 회원이 작성한 모든 게시글을 삭제한다.
    // 회원 삭제 전에 해당 회원의 게시글을 먼저 정리할 때 사용한다.
    @Transactional
    void deleteAllByMember(Member member);
}

// JpaRepository를 상속하면 save(), findById(), findAll(), delete() 같은
// 기본 CRUD 메서드를 별도로 구현하지 않아도 사용할 수 있다.
//
// deleteAllByMember()처럼 메서드 이름에 Entity의 필드명을 규칙에 맞게 작성하면
// Spring Data JPA가 조건에 맞는 쿼리를 자동으로 생성한다.
