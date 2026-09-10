package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

// 게시글 작성 화면에서 입력한 값을 전달하기 위한 Form DTO
@Data
// getter, setter, toString, equals, hashCode 등을 자동으로 생성한다.
@Builder
// Builder 패턴으로 객체를 생성할 수 있도록 한다.
@AllArgsConstructor
// 모든 필드를 매개변수로 받는 생성자를 자동으로 만든다.
@NotBlank
public class ArticleForm {

    // 게시글 수정 시 기존 게시글을 식별하기 위해 사용할 ID
    private Long id;

    // 게시글 제목을 입력받는다.
    // 값이 비어 있으면 지정한 메시지로 유효성 검사를 실패시킨다.
    @NotBlank(message = "게시글 제목을 입력하세요")
    private String title;

    // 게시글 내용을 입력받는다.
    // 값이 비어 있으면 지정한 메시지로 유효성 검사를 실패시킨다.
    @NotBlank(message = "게시글 내용을 입력하세요")
    private String description;
}
