package com.example.demo.repository;

import com.example.demo.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Member Entity의 DB 작업을 담당하는 Repository
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 이메일로 회원을 조회합니다.
    // Optional을 사용해서 회원이 없을 수도 있는 상황을 표현합니다.
    Optional<Member> findByEmail(String email);

    // JpaRepository를 상속받기 때문에
    // save(), findById(), findAll(), delete() 등의 기본 CRUD 메서드를 사용할 수 있습니다.
}
