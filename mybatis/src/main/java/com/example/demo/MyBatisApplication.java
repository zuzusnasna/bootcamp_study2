package com.example.demo;

import com.example.demo.mapper.ArticleMapper;
import com.example.demo.mapper.MemberMapper;
import com.example.demo.model.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyBatisApplication implements ApplicationRunner {

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private ArticleMapper articleMapper;

    /**
     * Callback used to run the bean.
     *
     * @param args incoming application arguments
     * @throws Exception on error
     */
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        var articles = articleMapper.selectAll();
//        log.info("게시글 목록={}", articles);
//        log.info("----------------------------------------------");
//
//    }


//    @Override
//    public void run(ApplicationArguments args) throws Exception{
//        var members = memberMapper.selectAll();
//        log.info("회원 목록 = {}", members);
//        log.info("---------------------------------------------");
//        var member = memberMapper.selectById(3L);
//        log.info("3번 회원 = {}", member);
//    }


      //예제
    @Override
    public void run(ApplicationArguments args) throws Exception{
        var member1 = memberMapper.selectByNameLike("서");
        log.info("이름에 서를 포함한 사람: {}", member1);

        memberMapper.insert(Member.builder()
                .name("김민준")
                .email("minjun@test.com")
                .age(25).build());

        var members = memberMapper.selectAll();
        log.info("회원 목록: {}", members);
    }
}
