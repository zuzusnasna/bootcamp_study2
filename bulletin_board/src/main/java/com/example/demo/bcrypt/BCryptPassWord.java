package com.example.demo.bcrypt;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptPassWord {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("1111"));
    }
}
