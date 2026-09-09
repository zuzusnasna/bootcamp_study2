package com.example.demo.controller;

import com.example.demo.model.Member;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApiController {
    private List<Member> members = List.of(
            Member.builder().id(1L).name("윤서준").email("Seojun@naver.com").build(),
            Member.builder().id(2L).name("윤광철").email("Kwangcheol@naver.com").build(),
            Member.builder().id(3L).name("공미영").email("Miyoung@naver.com").build(),
            Member.builder().id(4L).name("김도윤").email("Doyoon@naver.com").build()
    );

    @GetMapping("/api/members")
    public List<Member> getMembers(){
        return members;
    }
}
