package com.example.demo.service;

import com.example.demo.dto.MemberRequest;
import com.example.demo.dto.MemberResponse;
import com.example.demo.entity.Member;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    public MemberResponse create(MemberRequest memberRequest){
        var member = Member.builder()
                .name(memberRequest.getName())
                .email(memberRequest.getEmail())
                .age(memberRequest.getAge())
                .enabled(true).build();
        memberRepository.save(member);

        //Entity를 클라이언트에세 보낼 Response DTO로 변환
//        var  memberResponse = MemberResponse.builder()
//                .id(member.getId())
//                .name(member.getName())
//                .email(member.getEmail())
//                .age(member.getAge()).build();
//        return memberResponse;
          return mapToMemberResponse(member);
    }

    public List<MemberResponse> findAll(){
        return memberRepository.findAll().stream().map(this::mapToMemberResponse).toList();
    }

    @Transactional //데이터를 여러건(리스트 List로) 넣었을때 문제가 없다면 실행, 문제가 있다면 다시 반환(배치처리)
    public List<MemberResponse> createBatch(List<MemberRequest> memberRequests){
        return memberRequests.stream().map(this::create).toList();
    }
    private MemberResponse mapToMemberResponse(Member member){
        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .age(member.getAge()).build();
    }



    //id 검색
    public MemberResponse findById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundException::new);
        return mapToMemberResponse(member);
    }

    //수정
    public MemberResponse update(Long id, Member memberRequest) {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundException::new);
        member.setName(memberRequest.getName());
        member.setEmail(memberRequest.getEmail());
        member.setAge(memberRequest.getAge());
        memberRepository.save(member);
        return mapToMemberResponse(member);
    }

    //삭제
    public void delete(Long id){
        MemberRepository.findById(id);
        memberRepository.delete(member);
    }

}
