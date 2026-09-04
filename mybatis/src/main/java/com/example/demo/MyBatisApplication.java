package com.example.demo;

import com.example.demo.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

public class MyBatisApplication implements ApplicationRunner {

    @Autowired
    private MemberMapper memberMapper;


    @Override
    public void run(ApplicationArguments args) throws Exception{

    }
}
