package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 게시글 생성/수정 요청에 사용하는 Request DTO.
 *
 * 흐름:
 * Client JSON -> ArticleRequest -> ArticleService -> Article Entity
 *
 * HTTP 요청에서 필요한 게시글 입력값만 담는다.
 */
@Data // getter/setter 등 반복적인 메서드를 Lombok이 자동 생성
@Builder // builder 패턴으로 객체 생성 가능
@AllArgsConstructor // 모든 필드를 받는 생성자 생성
@NoArgsConstructor // JSON 요청을 객체로 변환할 때 사용할 기본 생성자
public class ArticleRequest {

    // 게시글 제목
    private String title;

    // 게시글 본문/설명
    private String description;
}
