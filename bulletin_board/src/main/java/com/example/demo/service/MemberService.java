package com.example.demo.service;

import com.example.demo.dto.MemberDTO;
import com.example.demo.model.Member;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.AuthorityRepository;
import com.example.demo.repository.MemberRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Builder
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final AuthorityRepository authorityRepository;

    public MemberDTO findById(Long Id){
        Member member =memberRepository.findById()
    }

}
