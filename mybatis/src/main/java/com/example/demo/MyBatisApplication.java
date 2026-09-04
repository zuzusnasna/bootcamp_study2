package com.example.demo;

import com.example.demo.mapper.MemberMapper;
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


    @Override
    public void run(ApplicationArguments args) throws Exception{
        var members = memberMapper.selectAll();
        log.info("회원 목록 = {}", members);
        log.info("---------------------------------------------");
        var member = memberMapper.selectById(3L);
        log.info("3번 회원 = {}", member);

    }
}
