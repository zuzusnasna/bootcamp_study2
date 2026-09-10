package com.example.demo.controller;

import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Spring MVC의 Controller로 등록한다.
// 사용자의 요청(URL)을 받아 알맞은 화면이나 다른 URL로 연결하는 역할을 한다.
@Controller
// final 필드에 대한 생성자를 Lombok이 자동으로 만들어준다.
// 생성자 주입 방식으로 MemberService를 주입받을 수 있다.
@RequiredArgsConstructor
public class HomeController {

    // 회원 관련 기능을 처리하는 서비스 객체를 주입받는다.
    // 현재 컨트롤러에서는 직접 사용하지 않지만 생성자 주입 구조로 등록되어 있다.
    private final MemberService memberService;

    // GET 방식으로 애플리케이션의 기본 경로(/)에 접근했을 때 실행된다.
    @GetMapping("/")
    public String getHome(){
        // 실제 홈 화면을 별도로 만들지 않고 게시글 목록 URL로 요청을 전달한다.
        // forward를 사용하므로 브라우저의 URL은 그대로 유지하면서 서버 내부에서 처리한다.
        return "forward:/article/list";
    }

    // GET 방식으로 /login 요청이 들어오면 실행된다.
    @GetMapping("/login")
    public String getLogin(){
        // login.html 화면을 보여주기 위해 View 이름을 반환한다.
        return "login";
    }

    // GET 방식으로 /logout 요청이 들어오면 실행된다.
    @GetMapping("/logout")
    public String getLogout(){
        // logout.html 화면을 보여주기 위해 View 이름을 반환한다.
        return "logout";
    }
}
