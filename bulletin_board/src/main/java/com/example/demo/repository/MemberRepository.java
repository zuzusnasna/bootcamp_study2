package com.example.demo.repository;

import com.example.demo.model.Member;

import java.util.List;

public interface MemberRepository {
    List<Member> findByEmail(String email);
}
