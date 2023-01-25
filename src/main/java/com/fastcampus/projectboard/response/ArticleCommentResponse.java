package com.fastcampus.projectboard.response;

import com.fastcampus.projectboard.dto.ArticleCommentDto;

import java.io.Serializable;

public record ArticleCommentResponse (
        Long id,
        String content,
        String createdBy,
        String userAccountEmail,
        String userAccountNickname
) implements Serializable {

    public static ArticleCommentResponse of (Long id, String content, String createdBy, String email, String nickname) {
        return new ArticleCommentResponse(id, content, createdBy, email, nickname);
    }

    public static ArticleCommentResponse from (ArticleCommentDto dto) {
        String nickname = dto.userAccountDto().nickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().user_id();
        }
        return new ArticleCommentResponse(
                dto.id(),
                dto.content(),
                dto.createdBy(),
                dto.userAccountDto().email(),
                nickname
        );
    }
}
