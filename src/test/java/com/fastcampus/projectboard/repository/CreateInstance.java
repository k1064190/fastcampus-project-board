package com.fastcampus.projectboard.repository;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.ArticleComment;
import com.fastcampus.projectboard.domain.UserAccount;
import com.fastcampus.projectboard.dto.ArticleCommentDto;
import com.fastcampus.projectboard.dto.ArticleDto;

public class CreateInstance {
    public static UserAccount createUserAccount() {
        return UserAccount.of("test", "test", "test", "test", "test");
    }

    public static Article createArticle(UserAccount userAccount) {
        return Article.of(userAccount, "test", "test", "test");
    }

    public static ArticleDto createArticleDto() {
        return ArticleDto.from(createArticle(createUserAccount()));
    }

    public static ArticleComment createArticleComment(Article article, UserAccount userAccount) {
        return ArticleComment.of(article, userAccount, "test");
    }

    public static ArticleComment createArticleComment(Article article, UserAccount userAccount, String content) {
        return ArticleComment.of(article, userAccount, content);
    }

    public static ArticleCommentDto createArticleCommentDto() {
        return ArticleCommentDto.from(createArticleComment(createArticle(createUserAccount()), createUserAccount()));
    }

    public static ArticleCommentDto createArticleCommentDto(String content) {
        return ArticleCommentDto.from(createArticleComment(createArticle(createUserAccount()), createUserAccount(), content));
    }
}
