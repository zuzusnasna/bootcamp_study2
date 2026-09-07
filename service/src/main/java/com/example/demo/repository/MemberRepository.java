package com.example.demo.repository;

import com.example.demo.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Member Entity의 DB 접근을 담당하는 Repository.
 *
 * 흐름:
 * MemberService -> MemberRepository -> JPA/Hibernate -> DB
 *
 * SQL을 직접 작성하지 않아도 JpaRepository가 제공하는 기본 CRUD 메서드를 사용할 수 있다.
 */
@Repository // Repository 계층의 Bean으로 등록
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * JpaRepository<Member, Long>의 의미
     *
     * Member : 이 Repository가 관리하는 Entity 타입
     * Long   : Member의 PK(id) 타입
     *
     * 따라서 다음과 같은 메서드를 기본으로 사용할 수 있다.
     * save(), findById(), findAll(), delete() 등
     */
}
