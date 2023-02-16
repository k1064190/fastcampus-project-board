package com.fastcampus.projectboard.response;

import com.fastcampus.projectboard.domain.Hashtag;
import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.dto.HashtagDto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record ArticleResponse(
        Long id,
        String title,
        String content,
        Set<Hashtag> hashtags,
        LocalDateTime createdAt,
        String email,
        String nickname
) {

    public static ArticleResponse of(Long id, String title, String content, Set<Hashtag> hashtags, LocalDateTime createdAt, String email, String nickname) {
        return new ArticleResponse(id, title, content, hashtags, createdAt, email, nickname);
    }

    public static ArticleResponse from(ArticleDto dto) {
        String nickname = dto.userAccountDto().nickname();
        if(nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().userId();
        }
        return new ArticleResponse(
                dto.id(),
                dto.title(),
                dto.content(),
                dto.hashtagDtos().stream()
                        .map(HashtagDto::toEntity)
                        .collect(Collectors.toCollection(LinkedHashSet::new)),
                dto.createdAt(),
                dto.userAccountDto().email(),
                nickname
        );
    }
}
