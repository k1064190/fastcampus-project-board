package com.fastcampus.projectboard;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.ArticleComment;
import com.fastcampus.projectboard.domain.UserAccount;
import com.fastcampus.projectboard.dto.ArticleCommentDto;
import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.dto.ArticleUpdateDto;
import com.fastcampus.projectboard.dto.UserAccountDto;
import org.h2.engine.User;

import java.time.LocalDateTime;

public class createInstances {
    public static ArticleDto createArticleDto() {
        return ArticleDto.of(Long.valueOf(1), createUserAccountDto(), "title", "content", "nickname", LocalDateTime.now(), "Gagasbandas");
    }

    public static UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(Long.valueOf(1), "username", "password", "Gagasbandas@gmail.com", "Gagasbandas", "memo", LocalDateTime.now(), "Gagasbandas");
    }

    public static UserAccount createUserAccount() {
        return UserAccount.of("gagasbandas", "username", "password", "gagasbandas", "memo");
    }

    public static ArticleComment createArticleComment(String content) {
        return ArticleComment.of(createUserAccount(), createArticle(), content);
    }
    public static Article createArticle() {
        return Article.of(createUserAccount(), "title", "content", "hashtag");
    }

    public static ArticleCommentDto createArticleCommentDto() {
        return ArticleCommentDto.of(Long.valueOf(1), createArticleDto(), createUserAccountDto(), "content", LocalDateTime.now(), "gagasbandas");
    }

    public static ArticleUpdateDto createArticleUpdateDto() {
        return ArticleUpdateDto.of("title", "content", "hashtag");
    }
}
