package com.example.demo.controller;

import com.example.demo.dto.MemberDTO;
import com.example.demo.dto.MemberForm;
import com.example.demo.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

// 회원 목록 조회, 회원 정보 수정, 회원 삭제 요청을 처리하는 Controller이다.
// 실제 회원 데이터의 조회/수정/삭제 작업은 MemberService가 담당한다.
@Controller
// final 필드를 생성자로 주입받을 수 있도록 생성자를 자동으로 만들어 준다.
@RequiredArgsConstructor
// 이 Controller의 요청 URL 앞에 '/member'를 붙인다.
@RequestMapping("/member")
public class MemberController {

    // 회원 관련 비즈니스 로직을 처리하는 Service를 주입받는다.
    private final MemberService memberService;

    // '/member/list'로 들어오는 GET 요청을 처리하여 회원 목록을 조회한다.
    @GetMapping("/list")
    public String getMemberList(
            // 한 페이지에 10명의 회원을 표시하고 ID를 기준으로 내림차순 정렬한다.
            @PageableDefault(
                    size = 10,
                    sort = "id",
                    direction = Sort.Direction.DESC)
            // 요청 URL의 page, size 등의 정보를 바탕으로 페이징 조건을 전달받는다.
            Pageable pageable,
            // 조회한 회원 목록을 View에 전달하기 위한 객체이다.
            Model model){

        // 페이징 조건을 Service에 전달하여 회원 목록을 조회한다.
        Page<MemberDTO> page = memberService.findAll(pageable);

        // 조회한 회원 페이지 정보를 'page'라는 이름으로 View에 전달한다.
        model.addAttribute("page", page);
        // Thymeleaf가 회원 목록 화면을 렌더링하도록 View 이름을 반환한다.
        return "member-list";
    }

    // '/member/edit?id=회원ID'로 들어오는 GET 요청을 처리하여 회원 수정 화면을 보여준다.
    @GetMapping("/edit")
    public String getMemberEdit(
            // 수정할 회원의 ID를 요청 파라미터로 전달받는다.
            @RequestParam("id") Long id,
            // 수정 화면에서 사용할 MemberForm 객체를 생성한다.
            @ModelAttribute("member") MemberForm memberForm){

        // 회원 ID를 Service에 전달하여 DB에 저장된 기존 회원 정보를 조회한다.
        MemberDTO memberDTO = memberService.findById(id);

        // 조회한 회원 정보를 수정 폼에 채워 기존 값을 화면에 표시한다.
        memberForm.setId(memberDTO.getId());
        memberForm.setEmail(memberDTO.getEmail());
        memberForm.setName(memberDTO.getName());

        // Thymeleaf가 회원 수정 화면을 렌더링하도록 View 이름을 반환한다.
        return "member-edit";
    }

    // '/member/edit'로 들어오는 POST 요청을 처리하여 회원 정보를 수정한다.
    @PostMapping("/edit")
    public String postMemberEdit(
            // 수정 폼 데이터를 MemberForm에 담고 @Valid를 통해 입력값을 검증한다.
            @Valid @ModelAttribute("member") MemberForm memberForm,
            // @Valid에서 발생한 검증 오류를 보관한다.
            BindingResult bindingResult) {

        // 입력값 검증에 오류가 있으면 수정 작업을 실행하지 않고 수정 화면으로 돌아간다.
        if (bindingResult.hasErrors()) {
            return "member-edit";
        }

        // 검증을 통과한 회원 정보를 Service에 전달하여 실제 회원 정보를 수정한다.
        memberService.patch(memberForm);
        // 수정이 완료되면 회원 목록으로 이동한다.
        return "redirect:/member/list";
    }

    // '/member/delete?id=회원ID'로 들어오는 GET 요청을 처리하여 회원 삭제를 요청한다.
    @GetMapping("/delete")
    public String getMemberDelete(
            // 삭제할 회원의 ID를 요청 파라미터로 전달받는다.
            @RequestParam("id") Long id) {

        // 회원 ID를 Service에 전달하여 회원과 관련된 삭제 작업을 수행한다.
        memberService.deleteById(id);
        // 삭제가 완료되면 회원 목록으로 이동한다.
        return "redirect:/member/list";
    }
}
