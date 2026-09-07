package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 게시글 조회 결과를 클라이언트에 전달하기 위한 Response DTO.
 *
 * 흐름:
 * DB -> Article/Member Entity -> ArticleService -> ArticleResponse -> JSON
 *
 * 게시글 자체의 정보뿐 아니라 작성자 정보도 함께 담아서
 * 한 번의 응답으로 화면에서 필요한 데이터를 전달할 수 있다.
 */
@Data // getter/setter, toString 등의 메서드를 자동 생성
@Builder // Service에서 필요한 응답 객체를 builder 방식으로 생성
public class ArticleResponse {

    // 게시글 PK
    private Long id;

    // 게시글 작성자의 회원 PK
    private Long memberId;

    // 게시글 작성자의 이름
    private String name;

    // 게시글 작성자의 이메일
    private String email;

    // 게시글 제목
    private String title;

    // 게시글 본문/설명
    private String description;

    // 게시글 생성 시각
    private Date created;

    // 게시글 마지막 수정 시각
    private Date updated;
}
