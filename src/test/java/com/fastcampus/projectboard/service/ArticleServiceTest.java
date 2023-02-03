package com.fastcampus.projectboard.service;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.constant.SearchType;
import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.repository.ArticleRepository;
import com.fastcampus.projectboard.repository.UserAccountRepository;
import com.fastcampus.projectboard.util.CreateInstance;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks
    private ArticleService sut;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private UserAccountRepository userAccountRepository;

    @DisplayName("검색어 없이 게시글을 검색하면, 게시글 페이지를 반환한다.")
    @Test
    void givenNothing_whenSearchingArticles_thenReturnsArticlePage() {
        // Given
        Pageable pageable = Pageable.ofSize(20);
        given(articleRepository.findAll(pageable)).willReturn(Page.empty());
        // When
        Page<ArticleDto> articles = sut.searchArticles(null, null, pageable);
        // Then
        assertThat(articles).isEmpty();
        then(articleRepository).should().findAll(pageable);
    }

    @DisplayName("검색어 없이 게시글을 해시태그 검색하면, 빈 페이지를 반환한다.")
    @Test
    void givenNoSearchParameter_whenSearchingArticlesViaHashtag_thenReturnsEmptyPage() {
        // Given
        Pageable pageable = Pageable.ofSize(20);
        // When
        Page<ArticleDto> articles = sut.searchArticlesViaHashtag(null, pageable);
        // Then
        assertThat(articles).isEqualTo(Page.empty(pageable));
        then(articleRepository).shouldHaveNoInteractions();
    }

    @DisplayName("게시글을 해시태그 검색하면, 게시글 페이지를 반환한다.")
    @Test
    void givenSearchParameter_whenSearchingArticlesViaHashtag_thenReturnsArticlesPage() {
        // Given
        String hashtag = "#java";
        Pageable pageable = Pageable.ofSize(20);
        given(articleRepository.findByHashtag(hashtag, pageable)).willReturn(Page.empty(pageable));
        // When
        Page<ArticleDto> articles = sut.searchArticlesViaHashtag(hashtag, pageable);
        // Then
        assertThat(articles).isEqualTo(Page.empty(pageable));
        then(articleRepository).should().findByHashtag(hashtag, pageable);
    }

    @DisplayName("해시태그를 조회하면, 유니크 해시태그 리스트를 반환한다..")
    @Test
    void givenNothing_whenCalling_thenReturnsHashtags() {
        // Given
        List<String> expectedHashtags = List.of("#java", "#spirng", "#boot");
        given(articleRepository.findAllDistinctHashtags()).willReturn(expectedHashtags);
        // When
        List<String> actualHashtags = sut.getHashtags();
        // Then
        assertThat(actualHashtags).isEqualTo(expectedHashtags);
        then(articleRepository).should().findAllDistinctHashtags();
    }

    @DisplayName("검색어와 함께 게시글을 검색하면, 검색된 게시글 페이지를 반환한다.")
    @Test
    void givenSearchKeyword_whenSearchingArticles_thenReturnsSearchedArticlePage() {
        // Given
        Pageable pageable = Pageable.ofSize(20);
        SearchType searchType = SearchType.TITLE;
        String keyword = "keyword";
        given(articleRepository.findByTitleContaining(keyword, pageable)).willReturn(Page.empty());
        // When
        Page<ArticleDto> articles = sut.searchArticles(searchType, "keyword", pageable);
        // Then
        assertThat(articles).isEmpty();
        then(articleRepository).should().findByTitleContaining(keyword, pageable);
    }

    @DisplayName("게시글을 조회하면, 게시글을 반환한다.")
    @Test
    void givenArticleId_whenSearchingArticle_thenReturnsArticle() {
        // Given
        Long articleId = 1L;
        Article article = CreateInstance.createArticle(CreateInstance.createUserAccount());
        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));
        // When
        ArticleDto dto = sut.getArticle(articleId);
        // Then
        assertThat(dto)
                .hasFieldOrPropertyWithValue("title", article.getTitle())
                .hasFieldOrPropertyWithValue("content", article.getContent())
                .hasFieldOrPropertyWithValue("hashtag", article.getHashtag());
        then(articleRepository).should().findById(articleId);
    }

    @DisplayName("없는 게시글을 조회하면, 예외를 던진다.")
    @Test
    void givenNonExistentArticleId_whenSearchingArticle_thenThrowsException() {
        // Given
        Long articleId = 1L;
        given(articleRepository.findById(articleId)).willThrow(new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
        // When
        Throwable t = catchThrowable(() -> {
            sut.getArticle(articleId);
        });
        // Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("게시글이 없습니다 - articleId: " + articleId);
        then(articleRepository).should().findById(articleId);
    }

    @DisplayName("게시글을 입력하면, 게시글을 생성한다.")
    @Test
    void givenArticleInfo_whenCreatingArticle_thenSavesArticle() {
        // Given
        ArticleDto articleDto = CreateInstance.createArticleDto();
        given(articleRepository.save(any(Article.class))).willReturn(null);
        // When
        sut.saveArticle(articleDto);
        // Then
        then(articleRepository).should().save(any(Article.class));
    }

    @DisplayName("게시글의 수정 정보를 입력하면, 게시글을 수정한다.")
    @Test
    void givenModifiedArticleInfo_whenUpdatingArticle_thenUpdatesArticle() {
        // Given
        Long articleId = 1L;
        ArticleDto articleDto = CreateInstance.createArticleDto();
        Article article = CreateInstance.createArticle(CreateInstance.createUserAccount());
        given(articleRepository.getReferenceById(articleId)).willReturn(article);
        // When
        sut.updateArticle(articleId, articleDto);
        // Then
        assertThat(article)
                .hasFieldOrPropertyWithValue("title", articleDto.title())
                .hasFieldOrPropertyWithValue("content", articleDto.content())
                .hasFieldOrPropertyWithValue("hashtag", articleDto.hashtag());
        then(articleRepository).should().getReferenceById(articleId);
    }

    @DisplayName("없는 게시글의 수정 정보를 입력하면, 경고 로그를 찍고 아무것도 하지 않는다.")
    @Test
    void givenNonExistentModifiedArticleInfo_whenUpdatingArticle_thenLogsWarningAndDoesNothing() {
        // Given
        Long articleId = 1L;
        ArticleDto articleDto = CreateInstance.createArticleDto();
        given(articleRepository.getReferenceById(articleId)).willThrow(EntityNotFoundException.class);
        // When
        sut.updateArticle(articleId, articleDto);
        // Then
        then(articleRepository).shouldHaveNoMoreInteractions();
        then(articleRepository).should().getReferenceById(articleId);
    }

    @DisplayName("게시글의 ID를 입력하면, 게시글을 삭제한다.")
    @Test
    void givenArticleId_whenDeletingArticle_thenDeletesArticle() {
        // Given
        Long articleId = 1L;
        willDoNothing().given(articleRepository).deleteById(articleId);
        // When
        sut.deleteArticle(articleId);
        // Then
        then(articleRepository).should().deleteById(articleId);
    }
}
