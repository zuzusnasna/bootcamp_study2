package com.example.demo.repository;

import com.example.demo.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ArticleRepository extends JpaRepository<Member , Long> {
    @Transactional
    void deleteAllByMember(Member member);
}
//JpaRepository를 상속하면 save(), findById(), findAll(),
//delete() 같은 기본 CRUD 메서드를 바로 사용할 수
//있습니다. findByEmail(), findByMember()는 메서드 이름으로
//쿼리를 만드는 Spring Data JPA 방식입니다.