package com.fastcampus.projectboard.service;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.ArticleComment;
import com.fastcampus.projectboard.domain.UserAccount;
import com.fastcampus.projectboard.dto.*;
import com.fastcampus.projectboard.repository.ArticleCommentRepository;
import com.fastcampus.projectboard.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - 댓글")
@ExtendWith(MockitoExtension.class)
class ArticleCommentServiceTest {

    @InjectMocks private ArticleCommentService sut;

    @Mock private ArticleRepository articleRepository;
    @Mock private ArticleCommentRepository articleCommentRepository;

    @DisplayName("게시글 ID로 조회하면, 해당하는 댓글 리스트를 반환한다.")
    @Test
    void givenArticleId_whenSearchingArticleComments_thenReturnsArticleComments() {
        // Given
        Long articleId = 1L;
        ArticleComment expected = createArticleComment("content");
        given(articleCommentRepository.findByArticle_Id(articleId)).willReturn(List.of(expected));
        // When
        List<ArticleCommentDto> dto = sut.searchArticleComment(1L);

        // Then
        assertThat(dto).isNotNull();
        then(articleRepository).should().findById(articleId);
    }

    @DisplayName("댓글 정보를 입력하면, 댓글을 생성한다.")
    @Test
    void givenArticleCommentInfo_whenSavingArticleComment_thenSavesArticleComments() {
        // Given
        Long articleId = 1L;
        ArticleCommentDto dto = createArticleCommentDto();
        given(articleCommentRepository.save(any(ArticleComment.class))).willReturn(null);
        // When
        sut.saveArticleComment(dto);

        // Then
        assertThat(dto).isNotNull();
        then(articleCommentRepository).should().save(any(ArticleComment.class));
    }

    @DisplayName("댓글 ID와 수정 정보를 입력하면, 댓글을 수정한다.")
    @Test
    void givenArticleCommentIdAndModifiedInfo_whenUpdatingArticleComments_thenUpdatesArticleComments() {
        // Given
        ArticleCommentUpdateDto dto = ArticleCommentUpdateDto.of("Gagasbandas");
        given(articleCommentRepository.save(any(ArticleComment.class))).willReturn(null);
        // When
        sut.updateArticleComment(1L, dto);
        // Then
        then(articleCommentRepository).should().save(any(ArticleComment.class));
    }

    @DisplayName("댓글의 ID를 입력하면, 댓글을 삭제한다.")
    @Test
    void givenArticleCommentId_whenDeletingArticleComment_thenDeletesArticleComments() {
        // Given
        willDoNothing().given(articleCommentRepository).delete(any(ArticleComment.class));
        // When
        sut.deleteArticleComment(1L);

        // Then
        then(articleCommentRepository).should().delete(any(ArticleComment.class));
    }

    private ArticleComment createArticleComment(String content) {
        return ArticleComment.of(createUserAccount(), createArticle(), content);
    }

    private UserAccount createUserAccount() {
        return UserAccount.of("gagasbandas", "password", "gagasbandas@gmail.com", "gagasbandas", null);
    }

    private Article createArticle() {
        return Article.of(createUserAccount(), "title", "content", "hashtag");
    }

    private ArticleCommentDto createArticleCommentDto() {
        return ArticleCommentDto.of(Long.valueOf(1), createArticleDto(), createUserAccountDto(), "content", LocalDateTime.now(), "gagasbandas");
    }

    private ArticleDto createArticleDto() {
        return ArticleDto.of(Long.valueOf(1), createUserAccountDto(), "title", "content", "hashtag", LocalDateTime.now(), "gagasbandas");
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(Long.valueOf(1), "gagasbandas", "password", "gagasbandas@gmail.com", "gagasbandas", "memo", LocalDateTime.now(), "gagasbandas");
    }
}