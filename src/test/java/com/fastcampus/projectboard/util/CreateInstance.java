package com.fastcampus.projectboard.util;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.ArticleComment;
import com.fastcampus.projectboard.domain.Hashtag;
import com.fastcampus.projectboard.domain.UserAccount;
import com.fastcampus.projectboard.dto.*;

import java.util.Set;

public class CreateInstance {
    public static UserAccount createUserAccount() {
        return UserAccount.of("createdByUserAccount", "test", "test", "test", "test");
    }

    public static UserAccount createUserAccount(String userId) {
        return UserAccount.of(userId, "test", "test", "test", "test");
    }

    public static UserAccountDto createUserAccountDto() {
        return UserAccountDto.of("createdByUserAccountDto", "test", "test", "test", "test");
    }

    public static UserAccountDto createUserAccountDto(String userId) {
        return UserAccountDto.of(userId, "test", "test", "test", "test");
    }
    public static Article createArticle(UserAccount userAccount) {
        Article article = Article.of(userAccount, "createdByArticle", "test");
        article.addHashtags(Set.of(Hashtag.of("test"), Hashtag.of("test2")));
        return article;
    }

    public static ArticleDto createArticleDto() {
        return ArticleDto.of(
                createUserAccountDto("createdByArticleDto"),
                "createdByArticleDto",
                "test",
                Set.of(HashtagDto.of("test"))
        );
    }

    public static ArticleDto createArticleDto(UserAccount userAccount, String title, String content) {
        return ArticleDto.of(
                createUserAccountDto(userAccount.getUserId()),
                title,
                content,
                Set.of()
        );
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

    public static ArticleWithCommentsDto createArticleWithCommentsDto() {
        return ArticleWithCommentsDto.from(createArticle(createUserAccount()));
    }
}
