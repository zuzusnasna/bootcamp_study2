package com.example.demo.repository;

import com.example.demo.model.Authority;
import com.example.demo.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// 회원의 권한(Authority) 데이터를 조회하고 저장하는 Repository
@Repository
public interface AuthorityRepository extends JpaRepository<Member, Long> {

    // 특정 회원에게 연결된 권한 목록을 조회
    // 메서드 이름을 기준으로 Spring Data JPA가 쿼리를 자동 생성
    List<Authority> findByMember(Member member);
}
