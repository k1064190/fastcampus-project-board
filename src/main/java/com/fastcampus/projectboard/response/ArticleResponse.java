package com.fastcampus.projectboard.response;

import com.fastcampus.projectboard.dto.ArticleDto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record ArticleResponse(Long id, String title, String content, LocalDateTime createdAt, String email, String nickname) implements Serializable {

    public static ArticleResponse of (Long id, String title, String content, LocalDateTime createdAt, String email, String nickname) {
        return new ArticleResponse(id, title, content, createdAt, email, nickname);
    }

    public static ArticleResponse from (ArticleDto dto) {
        String nickname = dto.userAccountDto().nickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().user_id();
        }
        return new ArticleResponse(
                dto.id(),
                dto.title(),
                dto.content(),
                dto.createdAt(),
                dto.userAccountDto().email(),
                nickname
        );
    }
}
