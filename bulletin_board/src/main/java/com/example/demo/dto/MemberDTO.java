package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

//Member 엔티티에는 password가 있지만 DTO에 없는이유는
//화면 정보에 비밀번호를 노출하지 않기 위함
@Data
@Builder
public class MemberDTO {
    private Long id;
    private String name;
    private String email;
}


//        Member
//→ DB와 연결되는 객체(Entity)
//
//        MemberDTO
//→ Controller ↔ Service ↔ View 사이에서 데이터를 전달하는 객체

//-> 화면에 보여줄 내용과 보여주면 안되는 내용 필터링
//-> 하지만 DB에는 비밀번호를 가지고있어야 하기때문에
//-> Member 객체에는 비밀번호 포함 (Entity)
//-> 화면으로 전달될 내용인 MemberDTO에는 비밀번호가 없음