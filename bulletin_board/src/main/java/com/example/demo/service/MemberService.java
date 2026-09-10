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

// 회원과 관련된 비즈니스 로직을 처리하는 Service 클래스이다.
// Controller가 직접 Repository를 호출하지 않고 Service를 거치도록 하여
// 회원가입, 회원 조회 같은 실제 업무 처리를 한곳에서 담당한다.
@Builder
@Service
@RequiredArgsConstructor
public class MemberService {
    // 회원 정보를 DB에서 조회하거나 저장하기 위한 Repository이다.
    private final MemberRepository memberRepository;

    // 게시글과 회원의 연관 관계를 처리하기 위한 Repository이다.
    // 현재 클래스에서는 직접 사용하지 않지만 기존 회원 관련 기능과 연결되어 있다.
    private final ArticleRepository articleRepository;

    // 회원의 권한 정보를 조회하거나 처리하기 위한 Repository이다.
    // 현재 클래스에서는 직접 사용하지 않지만 회원 기능 확장을 위해 주입되어 있다.
    private final AuthorityRepository authorityRepository;

    // 회원가입 시 평문 비밀번호를 안전하게 암호화하기 위해 사용한다.
    // 비밀번호를 DB에 그대로 저장하지 않는 것이 중요하다.
    private final PasswordEncoder passwordEncoder;

    // 회원 번호로 회원을 조회한다.
    public MemberDTO findById(Long Id){
        return memberRepository.findById(Id)
                .map(this::mapToMemberDTO)
                .orElseThrow();
    }

    // Entity인 Member를 화면이나 다른 계층에서 사용하는 DTO로 변환한다.
    // DB Entity를 그대로 전달하지 않고 필요한 데이터만 DTO로 전달하기 위해 사용한다.
    private MemberDTO mapToMemberDTO(Member member){
        return MemberDTO.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .build();
    }

    // 회원가입에 필요한 회원 정보를 받아 새로운 회원을 생성한다.
    public MemberDTO create(MemberForm memberForm){

        // 회원가입 폼의 데이터를 실제 DB에 저장할 Member Entity로 변환한다.
        Member member = Member.builder()
                .name(memberForm.getName())
                // 비밀번호는 DB에 저장하기 전에 반드시 암호화한다.
                .password(passwordEncoder.encode(memberForm.getPassword()))
                .email(memberForm.getEmail())
                .build();

        // 완성된 Member Entity를 Repository를 통해 DB에 저장한다.
        memberRepository.save(member);

        // 저장된 회원 Entity를 DTO로 변환하여 반환한다.
        return mapToMemberDTO(member);
    }

    // 이메일로 회원을 조회한다.
    // 회원이 존재하지 않을 수도 있으므로 Optional로 결과를 감싼다.
    public Optional<MemberDTO> findByEmail(String email){
        return memberRepository.findByEmail(email)
                // 조회된 Entity가 있다면 MemberDTO로 변환한다.
                .map(this::mapToMemberDTO);
    }

}
