package com.example.demo.service;

import com.example.demo.dto.MemberDTO;
import com.example.demo.dto.MemberForm;
import com.example.demo.model.Member;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.AuthorityRepository;
import com.example.demo.repository.MemberRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Builder
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberDTO findById(Long Id){
        return memberRepository.findById(Id)
                .map(this::mapToMemberDTO)
                .orElseThrow();
    }

    private MemberDTO mapToMemberDTO(Member member){
        return MemberDTO.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .build();
    }

    public MemberDTO create(MemberForm memberForm){

        Member member = Member.builder()
                .name(memberForm.getName())
                .password(passwordEncoder.encode (memberForm.getPassword()))
                .email(memberForm.getEmail())
                .build();

        memberRepository.save(member);
        return mapToMemberDTO(member);
    }

    public Optional<MemberDTO> findByEmail(String email){
        return memberRepository.findByEmail(email)
                .map(this::mapToMemberDTO);
    }

}
