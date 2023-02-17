package com.fastcampus.projectboard.response;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.ArticleComment;
import com.fastcampus.projectboard.domain.Hashtag;
import com.fastcampus.projectboard.domain.UserAccount;
import com.fastcampus.projectboard.dto.ArticleCommentDto;
import com.fastcampus.projectboard.dto.ArticleWithCommentsDto;
import com.fastcampus.projectboard.dto.HashtagDto;
import com.fastcampus.projectboard.dto.UserAccountDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@DisplayName("DTO - 댓글을 포함한 게시글 응답 테스트")
@ExtendWith(MockitoExtension.class)
class ArticleWithCommentsResponseTest {

    @DisplayName("자식 댓글이 없는 게시글 + 댓글 dto를 api 응답으로 변환할 때, 댓글을 시간 내림차순 + ID 오름차순으로 정리한다.")
    @Test
    void givenArticleWithCommentsDtoWithoutChildComments_whenMapping_thenOrganizesCommentsWithCertainOrder() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Set<ArticleCommentDto> articleCommentDtos = Set.of(
                createArticleCommentDto(1L, null, now),
                createArticleCommentDto(2L, null, now.plusDays(1)),
                createArticleCommentDto(3L, null, now.plusDays(3)),
                createArticleCommentDto(4L, null, now),
                createArticleCommentDto(5L, null, now.minusDays(2)),
                createArticleCommentDto(6L, null, now.minusDays(1)),
                createArticleCommentDto(7L, null, now.plusDays(7))
        );
        ArticleWithCommentsDto articleWithCommentsDto = createArticleWithCommentsDto(articleCommentDtos);

        // When
        ArticleWithCommentsResponse actual = ArticleWithCommentsResponse.from(articleWithCommentsDto);

        // Then
        assertThat(actual.articleCommentResponses())
                .containsExactly(
                        createArticleCommentResponse(7L, null, now.plusDays(7)),
                        createArticleCommentResponse(3L, null, now.plusDays(3)),
                        createArticleCommentResponse(2L, null, now.plusDays(1)),
                        createArticleCommentResponse(1L, null, now),
                        createArticleCommentResponse(4L, null, now),
                        createArticleCommentResponse(6L, null, now.minusDays(1)),
                        createArticleCommentResponse(5L, null, now.minusDays(2))
                );

    }

    @DisplayName("게시글 + 댓글 dto를 api 응답으로 변환할 때, 댓글 부모 자식 관계를 각각의 규칙으로 정렬하여 정리한다.")
    @Test
    void givenArticleWithCommentsDto_whenMapping_thenOrganizesParentAndChildCommentsWithCertainOrders() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Set<ArticleCommentDto> articleCommentDtos = Set.of(
                createArticleCommentDto(1L, null, now),
                createArticleCommentDto(2L, 1L, now.plusDays(1)),
                createArticleCommentDto(3L, 1L, now.plusDays(3)),
                createArticleCommentDto(4L, 1L, now),
                createArticleCommentDto(5L, null, now.minusDays(2)),
                createArticleCommentDto(6L, 5L, now.minusDays(1)),
                createArticleCommentDto(7L, 5L, now.plusDays(7))
        );
        ArticleWithCommentsDto input = createArticleWithCommentsDto(articleCommentDtos);

        // When
        ArticleWithCommentsResponse actual = ArticleWithCommentsResponse.from(input);

        // Then
        assertThat(actual.articleCommentResponses())
                .containsExactly(
                        createArticleCommentResponse(1L, null, now),
                        createArticleCommentResponse(5L, null, now.minusDays(2))
                )
                .flatExtracting(ArticleCommentResponse::childComments)
                .containsExactly(
                        createArticleCommentResponse(4L, 1L, now),
                        createArticleCommentResponse(2L, 1L, now.plusDays(1)),
                        createArticleCommentResponse(3L, 1L, now.plusDays(3)),
                        createArticleCommentResponse(6L, 5L, now.minusDays(1)),
                        createArticleCommentResponse(7L, 5L, now.plusDays(7))
                );

    }

    @DisplayName("게시글 + 댓글 dto를 api 응답으로 변환할 때, 부모 자식 깊이(depth)는 제한이 없다.")
    @Test
    void givenArticleWithCommentDtos_whenMapping_thenOrganizesParentAndChildCommentsWithoutDepthLimit() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Set<ArticleCommentDto> articleCommentDtos = Set.of(
                createArticleCommentDto(1L, null, now),
                createArticleCommentDto(2L, 1L, now.plusDays(1)),
                createArticleCommentDto(3L, 2L, now.plusDays(2)),
                createArticleCommentDto(4L, 3L, now.plusDays(3)),
                createArticleCommentDto(5L, 4L, now.plusDays(4)),
                createArticleCommentDto(6L, 5L, now.plusDays(5)),
                createArticleCommentDto(7L, 6L, now.plusDays(6))
        );
        ArticleWithCommentsDto input = createArticleWithCommentsDto(articleCommentDtos);

        // When
        ArticleWithCommentsResponse actual = ArticleWithCommentsResponse.from(input);

        // Then
        Iterator<ArticleCommentResponse> iterator = actual.articleCommentResponses().iterator();
        long i = 1L;
        while(iterator.hasNext()) {
            ArticleCommentResponse articleCommentResponse = iterator.next();
            assertThat(articleCommentResponse)
                    .hasFieldOrPropertyWithValue("id", i)
                    .hasFieldOrPropertyWithValue("parentCommentId", i == 1L ? null : i - 1L)
                    .hasFieldOrPropertyWithValue("createdAt", now.plusDays(i - 1L));

            iterator = articleCommentResponse.childComments().iterator();
            ++i;
        }

    }


    private ArticleCommentDto createArticleCommentDto(String content) {
        return ArticleCommentDto.of(
                1L,
                1L,
                createUserAccountDto(),
                content,
                LocalDateTime.now(),
                "uno",
                LocalDateTime.now(),
                "uno"
        );
    }

    private ArticleCommentDto createArticleCommentDto(Long id, Long parentCommentId, LocalDateTime createdAt) {
        return ArticleCommentDto.of(
                id,
                1L,
                createUserAccountDto(),
                parentCommentId,
                "content",
                createdAt,
                "uno",
                LocalDateTime.now(),
                "uno"
        );
    }

    private ArticleCommentResponse createArticleCommentResponse(Long id, Long parentCommentId, LocalDateTime createdAt) {
        return ArticleCommentResponse.of(
                id,
                "content",
                createdAt,
                "email",
                "nickname",
                "uno",
                parentCommentId
        );
    }

    private ArticleWithCommentsDto createArticleWithCommentsDto(Set<ArticleCommentDto> articleCommentDtos) {
        return ArticleWithCommentsDto.of(
                1L,
                createUserAccountDto(),
                articleCommentDtos,
                "title",
                "content",
                createHashtagDtos(),
                LocalDateTime.now(),
                "uno",
                LocalDateTime.now(),
                "uno"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "uno",
                "password",
                "uno@mail.com",
                "Uno",
                "This is memo",
                LocalDateTime.now(),
                "uno",
                LocalDateTime.now(),
                "uno"
        );
    }

    private UserAccount createUserAccount() {
        return UserAccount.of(
                "uno",
                "password",
                "uno@email.com",
                "Uno",
                null
        );
    }

    private Article createArticle() {
        Article article = Article.of(
                createUserAccount(),
                "title",
                "content"
        );
        article.addHashtags(Set.of(createHashtag(article)));

        return article;
    }

    private Hashtag createHashtag(Article article) {
        return Hashtag.of("java");
    }
    private Set<HashtagDto> createHashtagDtos() {
        return Set.of(HashtagDto.of("java"));
    }
}