package com.fastcampus.projectboard.domain;

import java.time.LocalDateTime;

public class Article {
    private Long id; // 게시글 번호
    private String title; // 게시글 제목
    private String content; // 게시글 내용
    private String hashtag; // 게시글 해시태그

    private LocalDateTime createdAt; // 게시글 작성일
    private LocalDateTime modifiedAt; // 게시글 수정일
    private String createdBy; // 게시글 작성자
    private String modifiedBy; // 게시글 수정자
}
