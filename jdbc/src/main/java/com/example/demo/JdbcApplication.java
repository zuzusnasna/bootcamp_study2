package com.example.demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JdbcApplication implements ApplicationRunner {
    private final MemberRepository memberRepository;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        //insert
//        memberRepository.save(Member.builder()
//                .name("자비스")
//                .email("zavis@naver.com")
//                .age(23).build());
//
//        //update
//        Member member = Member.builder()
//                .name("원이")
//                .email("onee@naver.com")
//                .age(23).build();
//        memberRepository.save(member);
//
//        var members =  memberRepository.findAll();
//        log.info("{}", members);
//
//        member.setAge(11);
//        memberRepository.save(member);
//        log.info("{}",member);

        //find all members
//        var members = memberRepository.findAll();
//        log.info("{}",members);

        //find member by id
//        var member = memberRepository.findById(1L);
//        log.info("{}",member);

        //findByAgeGreaterThan
//        var member = memberRepository.findByAgeGreaterThan(20);
//        log.info("{}",member);

//        //삭제
//        memberRepository.deleteById(5L);
//        log.info("회원 삭제 완료 : id = {}", 5L);

        //예제
//        //1. 이순신회원을 save()로 저장한 결과를 member 변수에 받은 뒤,
//        //나이를 50세에서 35세로 수정하고 저장하세요.
//        Member member = memberRepository.save(Member.builder()
//                .name("이순신")
//                .email("leesoonsin@naver.com")
//                .age(50).build());
//
//        member.setAge(35);
//        memberRepository.save(member);
//        log.info("{}",member);
//
//        //2. 이메일이 "Yoon@kosa.or.kr"인 회원을 조회하는 코드를 작성하세요.
//        var member = memberRepository.findByEmail("Yoon@kosa.or.kr");
//        log.info("{}",member);
//
//        //3. 이름이 "윤광철"이고 이메일이 "Kwang@hanbit.co.kr"인 회원을 조회하세요.
//        //두 조건이 모두 만족되어야 합니다.
//        var member = memberRepository.findByNameAndEmail("윤광철", "Kwang@hanbit.co.kr");
//        log.info("{}",member);


        //4. 이름이 "정수빈"인 회원을 조회한 뒤, 조회된 회원들을 반복하면서 id를 이용하여 삭제하는 코드를 작성하세요.
        List<Member> members = memberRepository.findByName("정수빈");

        for (Member member : members) {
            log.info(" id={}, name={}", member.getId(), member.getName());

            memberRepository.deleteById(member.getId());
        }
    }
}