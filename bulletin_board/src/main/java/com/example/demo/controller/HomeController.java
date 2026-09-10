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
// 브라우저가 '/', '/login', '/logout', '/signup' 같은 주소로 요청을 보내면
// 이 클래스가 요청을 받아 적절한 페이지나 기능으로 연결해 준다.
// 실제 회원 데이터 처리는 MemberService가 담당하고,
// 이 클래스는 HTTP 요청을 받아 Service와 화면을 연결하는 역할을 한다.
@Controller
// final 필드를 생성자로 주입받을 수 있도록 생성자를 자동으로 만들어 준다.
@RequiredArgsConstructor
public class HomeController {

    // 회원가입 여부 확인과 회원가입 처리를 담당하는 Service를 주입받는다.
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

    // 사용자가 '/signup' 주소로 접속했을 때 회원가입 요청을 처리한다.
    @GetMapping("/signup")
    public String getMemberAdd(
            // @ModelAttribute로 signup 폼의 입력값을 MemberForm 객체에 담는다.
            // @Valid를 사용하면 MemberForm에 작성한 Validation 규칙도 함께 검사한다.
            @Valid @ModelAttribute("member") MemberForm memberForm,
            // Validation 결과를 담고 있으며, 오류가 있으면 회원가입 화면으로 다시 보낸다.
            BindingResult bindingResult) {

        // 비밀번호가 입력되지 않았거나 8자리보다 짧은 경우 오류를 추가한다.
        if (memberForm.getPassword() == null ||
                memberForm.getPassword().trim().length() < 8) {
            bindingResult.rejectValue(
                    "password",
                    "NotBlank",
                    "패스워드를 8글자 이상 입력하세요."
            );
        }

        // 비밀번호와 비밀번호 확인 값이 같은지 검사한다.
        if (!memberForm.getPassword().equals(memberForm.getPasswordConfirm())) {
            bindingResult.rejectValue(
                    "passwordConfirm",
                    "MissMatch",
                    "입력하신 패스워드가 다릅니다."
            );
        }

        // 입력한 이메일로 이미 가입된 회원이 있는지 확인한다.
        if (memberService.findByEmail(
                memberForm.getEmail()).isPresent()) {
            // 이미 사용 중인 이메일이면 email 필드에 검증 오류를 추가한다.
            bindingResult.rejectValue(
                    "email",
                    "AlreadyExist",
                    "사용중인 이메일 입니다."
            );
        }

        // 지금까지의 Validation 과정에서 오류가 하나라도 있으면
        // 회원가입 처리를 하지 않고 signup 화면으로 돌아간다.
        if (bindingResult.hasErrors()) {
            return "signup";
        }

        // 모든 검증을 통과하면 MemberService의 create()를 호출하여 회원을 저장한다.
        memberService.create(memberForm);

        // 회원가입이 완료되면 게시판의 첫 화면으로 이동한다.
        return "redirect:/";
    }

}
