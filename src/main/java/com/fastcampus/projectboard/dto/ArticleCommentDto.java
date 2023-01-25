package com.fastcampus.projectboard.dto;

import com.fastcampus.projectboard.domain.ArticleComment;

import java.time.LocalDateTime;

public record ArticleCommentDto(
        Long id,
        ArticleDto articleDto,
        UserAccountDto userAccountDto,
        String content,
        LocalDateTime createdAt,
        String createdBy
) {
    public static ArticleCommentDto of(Long id, ArticleDto articleDto, UserAccountDto userAccountDto, String content, LocalDateTime createdAt, String createdBy) {
        return new ArticleCommentDto(id, articleDto, userAccountDto, content, createdAt, createdBy);
    }

    public static ArticleCommentDto from(ArticleComment entity) {
        return new ArticleCommentDto(
                entity.getId(),
                ArticleDto.from(entity.getArticle()),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getCreatedBy()
        );
    }
}
