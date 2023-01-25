package com.fastcampus.projectboard.dto;

import java.time.LocalDateTime;

public record ArticleCommentUpdateDto(
        String content
) {
    public static ArticleCommentUpdateDto of (String content) {
        return new ArticleCommentUpdateDto(content);
    }
}
