package com.example.demo.repository;

import com.example.demo.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

//    String findByEmail(String email); //이메일을 통해 사용자를 검색
    Optional<Member> findByEmail(String email);
}
