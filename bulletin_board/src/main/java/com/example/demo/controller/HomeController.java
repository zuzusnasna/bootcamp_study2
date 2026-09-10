package com.example.demo.controller;

import com.example.demo.dto.MemberForm;
import com.example.demo.dto.PasswordForm;
import com.example.demo.model.MemberUserDetails;
import com.example.demo.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

// 게시판 사이트의 기본 페이지 이동 요청을 처리하는 Controller이다.
//
// 브라우저가 '/', '/login', '/logout', '/signup', '/password' 같은 주소로 요청을 보내면
// 이 클래스가 요청을 받아 적절한 페이지나 기능으로 연결해 준다.
// 실제 회원 데이터 처리는 Service가 담당하고,
// 이 클래스는 HTTP 요청을 받아 Service와 화면을 연결하는 역할을 한다.
@Controller
// final 필드를 생성자로 주입받을 수 있도록 생성자를 자동으로 만들어 준다.
@RequiredArgsConstructor
public class HomeController {

    // 회원가입과 비밀번호 변경에 필요한 기능을 처리하는 Service를 주입받는다.
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

    // 사용자가 '/signup' 주소로 접속했을 때 회원가입 화면을 보여준다.
    @GetMapping("/signup")
    public String getMemberAdd(
            // 회원가입 화면을 처음 열 때 입력값을 담을 MemberForm 객체를 생성한다.
            @ModelAttribute("member") MemberForm memberForm) {

        // 처음 화면을 보여주는 GET 요청에서는 입력값을 검증하거나 DB에 저장하지 않는다.
        return "signup";
    }

    // 회원가입 폼을 제출하면 '/signup'으로 들어오는 POST 요청을 처리한다.
    @PostMapping("/signup")
    public String postMemberAdd(
            // 회원가입 폼의 입력값을 MemberForm에 담고 Validation 규칙을 검사한다.
            @Valid @ModelAttribute("member") MemberForm memberForm,
            // @Valid 검사 결과와 직접 추가한 검증 오류를 담는다.
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

        // 비밀번호가 입력되어 있고 비밀번호 확인 값과 같은지 검사한다.
        if (memberForm.getPassword() == null ||
                !memberForm.getPassword().equals(memberForm.getPasswordConfirm())) {
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
        // 비밀번호 BCrypt 암호화는 MemberService에서 처리한다.
        memberService.create(memberForm);

        // 회원가입이 완료되면 게시판의 첫 화면으로 이동한다.
        return "redirect:/";
    }

    // 사용자가 '/password' 주소로 접속하면 비밀번호 변경 화면을 보여준다.
    @GetMapping("/password")
    public String getPassword(
            // 비밀번호 변경 폼을 PasswordForm 객체에 담아 화면에 전달한다.
            @ModelAttribute("password") PasswordForm passWordForm){
        // password.html 화면을 보여준다.
        return "password";
    }

    // 비밀번호 변경 폼을 제출했을 때 실행되는 POST 요청이다.
    @PostMapping("/password")
    public String postPassword(
            // PasswordForm의 입력값을 받고 @Valid로 필드에 설정된 검증을 수행한다.
            @Valid @ModelAttribute("password") PasswordForm passwordForm,
            // @Valid 검사 결과를 담는다. 오류가 있으면 비밀번호 변경을 진행하지 않는다.
            BindingResult bindingResult,
            // 현재 로그인한 사용자의 정보를 Spring Security에서 주입받는다.
            @AuthenticationPrincipal MemberUserDetails userDetails) {

        // 입력한 기존 비밀번호가 실제 회원의 비밀번호와 일치하는지 확인한다.
        if (!memberService.checkPassword(
                userDetails.getMemberId(),
                passwordForm.getOld())){

            // 기존 비밀번호가 틀리면 old 필드에 오류 메시지를 추가한다.
            bindingResult.rejectValue(
                    "old",
                    "MissMatch",
                    "비밀번호가 잘못 되었습니다."
            );
        }

        // 기존 비밀번호나 새 비밀번호 등의 검증에서 오류가 있으면
        // DB의 비밀번호를 변경하지 않고 password 화면으로 돌아간다.
        if (bindingResult.hasErrors()){
            return "password";
        }

        // 모든 검증을 통과하면 Service에서 새로운 비밀번호로 변경한다.
        memberService.updatePassword(
                userDetails.getMemberId(),
                passwordForm.getPassword()
        );

        // 비밀번호 변경이 완료되면 게시판의 첫 화면으로 이동한다.
        return "redirect:/";
    }
}
