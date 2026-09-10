package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// 이 클래스를 Spring MVC의 Controller로 등록한다.
// Controller는 클라이언트의 요청을 받아 적절한 화면(View)으로 연결하는 역할을 한다.
@Controller
public class HomeController {

    // GET 방식으로 /home 요청이 들어오면 이 메서드가 실행된다.
    @GetMapping("/home")
    public String getHome(){
        // "home"이라는 View 이름을 반환한다.
        // Thymeleaf 설정에 따라 templates/home.html을 찾아 화면으로 보여준다.
        return "home";
    }

    // GET 방식으로 /product 요청이 들어오면 이 메서드가 실행된다.
    @GetMapping("/product")
    public String getProduct(){
        // "product" View를 반환하여 templates/product.html을 보여준다.
        return "product";
    }

    // GET 방식으로 /member 요청이 들어오면 이 메서드가 실행된다.
    @GetMapping("/member")
    public String getMember(){
        // "member" View를 반환하여 templates/member.html을 보여준다.
        return "member";
    }
}
