package com.example.demo.controller;

import com.example.demo.dto.MemberRequest;
import com.example.demo.dto.MemberResponse;
import com.example.demo.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //진입지점을 명시
@RequestMapping("/members")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @PostMapping
    public List<MemberResponse> post(@RequestBody List<MemberRequest> memberRequest){
        return memberService.createBatch(memberRequest);
    }
    @GetMapping()
    public List<MemberResponse> get(){
        return memberService.findAll();
    }

    //id 로 검색 , 수정 , 삭제 기능 추가

    @GetMapping("/{id}") // id 검색
    public MemberResponse get(@PathVariable("id") Long id) {
        return memberService.findById(id);
    }

    @PutMapping("/{id}")
    public MemberResponse put(@PathVariable("id") Long id, @RequestBody MemberRequest memberRequest) {
        return memberService.update(id, memberRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        memberService.deleteById(id);
    }

    @PatchMapping("/{id}")
    public MemberResponse patch(@PathVariable("id") Long id, @RequestBody MemberRequest memberRequest){
        return memberService.patch(id, memberRequest);
    }
}
