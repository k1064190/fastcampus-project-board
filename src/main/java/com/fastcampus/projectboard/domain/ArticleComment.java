package com.fastcampus.projectboard.domain;

import java.time.LocalDateTime;

public class ArticleComment {
    private Long id; // 댓글 번호
    private Article article; // 댓글이 달린 게시글
    private String content; // 댓글 내용

    private LocalDateTime createdAt; // 댓글 작성일
    private LocalDateTime modifiedAt; // 댓글 수정일
    private String createdBy; // 댓글 작성자
    private String modifiedBy; // 댓글 수정자
}