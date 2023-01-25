package com.fastcampus.projectboard.dto;

import com.fastcampus.projectboard.domain.UserAccount;

import java.time.LocalDateTime;

public record UserAccountDto(
        Long id,
        String user_id,
        String user_password,
        String email,
        String nickname,
        String memo,
        LocalDateTime createdAt,
        String createdBy
) {
    public static UserAccountDto of(Long id, String user_id, String user_password, String email, String nickname, String memo, LocalDateTime createdAt, String createdBy) {
        return new UserAccountDto(id, user_id, user_password, email, nickname, memo, createdAt, createdBy);
    }

    public static UserAccountDto from(UserAccount entity) {
        return new UserAccountDto(
                entity.getId(),
                entity.getUser_id(),
                entity.getUser_password(),
                entity.getEmail(),
                entity.getNickname(),
                entity.getMemo(),
                entity.getCreatedAt(),
                entity.getCreatedBy()
        );
    }
}
