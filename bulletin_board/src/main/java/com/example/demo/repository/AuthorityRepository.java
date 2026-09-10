package com.example.demo.repository;

import com.example.demo.model.Authority;
import com.example.demo.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorityRepository extends JpaRepository<Member, Long> {
    List<Authority> findByMember(Member member);
}
