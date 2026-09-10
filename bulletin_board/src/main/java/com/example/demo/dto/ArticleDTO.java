package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

// 게시글 데이터를 화면이나 Controller/Service 사이에서 전달하기 위한 DTO
@Data
@Builder
public class ArticleDTO {
    // 게시글 식별자
    private Long id;

    // 게시글을 작성한 회원의 식별자
    private Long memberId;

    // 작성자의 이름
    private String name;

    // 작성자의 이메일
    private String email;

    // 게시글 제목
    private String title;

    // 게시글 내용
    private String description;

    // 게시글 생성 시간
    private Date created;

    // 게시글 마지막 수정 시간
    private Date updated;
}
