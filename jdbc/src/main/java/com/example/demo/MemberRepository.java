package com.example.demo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository //레포지터리로 사용함을 선언, 안써도 무방
public interface MemberRepository extends CrudRepository<Member, Long> {
    //제공하는 메서드
    //생성/수정 : save()
    //조회 : findById(), findAll
    //삭제 : deleteById(), deleteAll()
    //전체 개수 조회 : count()

    List<Member> findByName(String name);
    Member findByEmail(String email);
    List<Member> findByAgeGreaterThan(Integer age);
    List<Member> findByNameAndEmail(String name, String email);
}
