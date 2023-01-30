package com.fastcampus.projectboard.service;

import com.fastcampus.projectboard.domain.ArticleComment;
import com.fastcampus.projectboard.dto.ArticleCommentDto;
import com.fastcampus.projectboard.repository.ArticleCommentRepository;
import com.fastcampus.projectboard.repository.ArticleRepository;
import com.fastcampus.projectboard.repository.CreateInstance;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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

    @DisplayName("게시글 ID가 주어지면, 해당 게시글의 댓글 목록을 조회한다.")
    @Test
    void givenArticleId_whenRequestingArticleComments_ThenReturnsArticleComments() {
        // Given
        Long articleId = 1L;
        ArticleComment expected = CreateInstance.createArticleComment(
                CreateInstance.createArticle(CreateInstance.createUserAccount()),
                CreateInstance.createUserAccount()
        );
        given(articleCommentRepository.findByArticle_Id(articleId)).willReturn(List.of(expected));

        // When
        List<ArticleCommentDto> actual = sut.searchArticleComments(articleId);

        // Then
        assertThat(actual)
                .hasSize(1)
                .first().hasFieldOrPropertyWithValue("content", expected.getContent());
        then(articleCommentRepository).should().findByArticle_Id(articleId);

    }


    @DisplayName("댓글 정보가 주어지면, 해당 게시글에 댓글을 등록한다.")
    @Test
    void givenArticleCommentInfo_whenSavingArticleComment_thenSavesArticleComment() {
        // Given
        ArticleCommentDto dto = CreateInstance.createArticleCommentDto();
        given(articleRepository.getReferenceById(dto.articleId())).willReturn(CreateInstance.createArticle(CreateInstance.createUserAccount()));
        given(articleCommentRepository.save(any(ArticleComment.class))).willReturn(null);
        // When
        sut.saveArticleComment(dto);
        // Then
        then(articleRepository).should().getReferenceById(dto.articleId());
        then(articleCommentRepository).should().save(any(ArticleComment.class));
    }

    @DisplayName("댓글 저장을 시도했는데 맞는 게시글이 없으면 경고 로그를 찍고 아무것도 안한다.")
    @Test
    void givenNonExistentArticleCommentInfo_whenSavingArticleComment_thenLogsSituationAndDoesNothing() {
        // Given
        ArticleCommentDto dto = CreateInstance.createArticleCommentDto();
        given(articleRepository.getReferenceById(dto.articleId())).willThrow(EntityNotFoundException.class);
        // When
        sut.saveArticleComment(dto);
        // Then
        then(articleRepository).should().getReferenceById(dto.articleId());
        then(articleCommentRepository).shouldHaveNoInteractions();
    }



    @DisplayName("댓글 정보가 주어지면, 해당 게시글의 댓글을 수정한다.")
    @Test
    void givenArticleCommentInfo_whenUpdatingArticleComment_thenUpdateArticleComment() {
        // Given
        ArticleComment oldComment = CreateInstance.createArticleComment(
                CreateInstance.createArticle(CreateInstance.createUserAccount()),
                CreateInstance.createUserAccount()
        );
        ArticleCommentDto newDto = CreateInstance.createArticleCommentDto("new");
        given(articleCommentRepository.getReferenceById(newDto.id())).willReturn(oldComment);
        // When
        sut.updateArticleComment(newDto);
        // Then
        then(articleCommentRepository).should().getReferenceById(newDto.id());
    }

    @DisplayName("없는 댓글 정보를 수정하려고 하면, 경고 로그를 찍고 아무것도 안한다.")
    @Test
    void givenNonExistentArticleCommentInfo_whenUpdatingArticleComment_thenLogsSituationAndDoesNothing() {
        // Given
        ArticleCommentDto newDto = CreateInstance.createArticleCommentDto("new");
        given(articleCommentRepository.getReferenceById(newDto.id())).willThrow(EntityNotFoundException.class);
        // When
        sut.updateArticleComment(newDto);
        // Then
        then(articleCommentRepository).should().getReferenceById(newDto.id());
    }

    @DisplayName("댓글 ID가 주어지면, 해당 게시글의 댓글을 삭제한다.")
    @Test
    void givenArticleCommentId_whenDeletingArticleComment_thenDeleteArticleComment() {
        // Given
        Long commentId = 1L;
        willDoNothing().given(articleCommentRepository).deleteById(commentId);
        // When
        sut.deleteArticleComment(commentId);
        // Then
        then(articleCommentRepository).should().deleteById(commentId);
    }

}
