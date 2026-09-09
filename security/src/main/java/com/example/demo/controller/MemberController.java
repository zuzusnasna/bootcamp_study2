package com.example.demo.controller;

import com.example.demo.model.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Slf4j
public class MemberController {
    private List<Member> members = List.of(
            Member.builder().id(1L).name("윤서준").email("Seojun@naver.com").build(),
            Member.builder().id(2L).name("윤광철").email("Kwangcheol@naver.com").build(),
            Member.builder().id(3L).name("공미영").email("Miyoung@naver.com").build(),
            Member.builder().id(4L).name("김도윤").email("Doyoon@naver.com").build()
    );

    @GetMapping("/member/list")
    public String getMembers(Model model){
        model.addAttribute("members", members);
        return "member-list";
    }

    @GetMapping("/")
    public String home(){
        return "redirect:/member/list";
    }
}
