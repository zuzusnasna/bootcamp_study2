package com.example.demo.controller;

import com.example.demo.dto.MemberForm;
import com.example.demo.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

// 게시판 사이트의 기본 페이지 이동 요청을 처리하는 Controller이다.
//
// 브라우저가 '/', '/login', '/logout' 같은 주소로 요청을 보내면
// 이 클래스가 요청을 받아 적절한 페이지로 연결해 준다.
// 게시판의 실제 데이터 조회나 저장은 Service가 담당하고,
// 이 클래스는 주로 "어떤 URL 요청을 어떤 화면으로 연결할 것인지"를 담당한다.
@Controller
// final 필드를 생성자를 통해 주입받을 수 있도록 생성자를 자동으로 만들어 준다.
@RequiredArgsConstructor
public class HomeController {

    // 회원 관련 기능을 처리하는 Service를 주입받는다.
    // 현재 HomeController에서는 직접 사용하지 않지만,
    // 회원 기능과 연결되는 Controller에서 사용할 수 있도록 구성되어 있다.
    private final MemberService memberService;

    // 사이트의 기본 주소('/')로 접속했을 때 실행된다.
    @GetMapping("/")
    public String getHome() {
        // 게시판의 글 목록 페이지로 요청을 다시 전달한다.
        // 별도의 홈 화면을 만들지 않고 게시판 목록을 첫 화면으로 사용한다.
        return "forward:/article/list";
    }

    // 사용자가 '/login' 주소로 접속했을 때 실행된다.
    @GetMapping("/login")
    public String getLogin() {
        // login.html 화면을 보여주도록 View 이름을 반환한다.
        return "login";
    }

    // 사용자가 '/logout' 주소로 접속했을 때 실행된다.
    @GetMapping("/logout")
    public String getLogout() {
        // logout.html 화면을 보여주도록 View 이름을 반환한다.
        return "logout";
    }

    @GetMapping("/signup")
    public String getMemberAdd(
            @Valid @ModelAttribute("member") MemberForm memberForm,
            BindingResult bindingResult) {

        if (memberForm.getPassword() == null ||
                memberForm.getPassword().trim().length() < 8) {
            bindingResult.rejectValue(
                    "password",
                    "NotBlank",
                    "패스워드를 8글자 이상 입력하세요."
            );
        }
        if (!memberForm.getPassword().equals(memberForm.getPasswordConfirm())) {

            bindingResult.rejectValue(
                    "passwordConfirm",
                    "MissMatch",
                    "입력하신 패스워드가 다릅니다."
            );
        }

        if (memberService.findByEmail(
                memberForm.getEmail()).isPresent()) {

            bindingResult.rejectValue(
                    "email",
                    "AlreadyExist",
                    "사용중인 이메일 입니다."
            );
        }
        if (bindingResult.hasErrors()) {
            return "signup";
        }
        memberService.create(memberForm);
        return "redirect:/";
    }

}
