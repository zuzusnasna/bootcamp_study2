package com.example.demo.service;

import com.example.demo.dto.MemberDTO;
import com.example.demo.dto.MemberForm;
import com.example.demo.model.Member;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.AuthorityRepository;
import com.example.demo.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

// 회원과 관련된 비즈니스 로직을 처리하는 Service 클래스이다.
// Controller가 직접 Repository를 호출하지 않고 Service를 거치도록 하여
// 회원가입, 회원 조회, 비밀번호 변경 같은 실제 업무 처리를 담당한다.
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

    // 회원가입과 비밀번호 변경 시 비밀번호를 암호화하거나 비교하기 위해 사용한다.
    // 평문 비밀번호를 DB에 그대로 저장하지 않기 위해 필요하다.
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

    // 사용자가 입력한 기존 비밀번호가 DB에 저장된 비밀번호와 일치하는지 확인한다.
    public boolean checkPassword(Long id, String password){
        // 먼저 회원 번호로 변경 대상 회원을 조회한다.
        Member member = memberRepository.findById(id)
                .orElseThrow();

        // 평문으로 입력받은 비밀번호와 암호화되어 저장된 비밀번호를 비교한다.
        // 암호화된 문자열을 직접 비교하지 않고 PasswordEncoder의 matches()를 사용한다.
        return passwordEncoder.matches(
                password,
                member.getPassword()
        );
    }

    // 회원의 비밀번호를 새로운 비밀번호로 변경한다.
    public void updatePassword(Long id, String password){
        // 회원 번호로 비밀번호를 변경할 회원을 조회한다.
        Member member = memberRepository.findById(id)
                .orElseThrow();

        // 새로운 비밀번호도 DB에 저장하기 전에 반드시 암호화한다.
        member.setPassword(
                passwordEncoder.encode(password)
        );

        // 변경된 회원 정보를 DB에 저장한다.
        memberRepository.save(member);
    }

    // 페이징 조건에 맞는 회원 목록을 조회하여 DTO 페이지로 변환한다.
    public Page<MemberDTO> findAll(Pageable pageable){
        // Repository에서 페이징된 회원 Entity를 조회한다.
        return memberRepository.findAll(pageable)
                // 각 Member Entity를 MemberDTO로 변환하면서 페이징 정보는 유지한다.
                .map((this::mapToMemberDTO));
    }

    // 수정 폼에 입력된 값만 기존 회원 정보에 반영한다.
    public MemberDTO patch(MemberForm memberForm){
        // 수정할 회원의 ID로 기존 회원 Entity를 조회한다.
        Member member = memberRepository
                .findById(memberForm.getId())
                .orElseThrow();

        // 이름이 전달된 경우에만 기존 이름을 변경한다.
        if(memberForm.getName() != null){
            member.setName(memberForm.getName());
        }
        // 비밀번호가 전달된 경우 암호화한 뒤 기존 비밀번호를 변경한다.
        if (memberForm.getPassword() != null){
            member.setPassword(
                    passwordEncoder.encode(
                            memberForm.getPassword()
                    )
            );
        }
        // 이메일이 전달된 경우에만 기존 이메일을 변경한다.
        if (memberForm.getEmail() != null){
            member.setEmail(memberForm.getEmail());
        }

        // 변경된 회원 Entity를 DB에 저장한다.
        memberRepository.save(member);
        // 수정된 회원 정보를 DTO로 변환하여 반환한다.
        return mapToMemberDTO(member);
    }

    // 회원 삭제와 회원이 작성한 게시글 삭제를 하나의 트랜잭션으로 처리한다.
    @Transactional
    public void deleteById(Long id) {

        // 삭제할 회원의 ID로 기존 회원 Entity를 조회한다.
        Member member = memberRepository
                .findById(id)
                // 존재하지 않는 회원을 삭제하려는 경우 예외를 발생시킨다.
                .orElseThrow();

        // 회원이 작성한 게시글을 먼저 삭제하여 회원과 게시글 사이의 연관 관계 문제를 방지한다.
        articleRepository.deleteAllByMember(member);
        // 관련 게시글 삭제가 끝나면 회원 정보를 삭제한다.
        memberRepository.delete(member);
    }
}
