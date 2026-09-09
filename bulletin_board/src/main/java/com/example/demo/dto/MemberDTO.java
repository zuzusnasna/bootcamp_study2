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
