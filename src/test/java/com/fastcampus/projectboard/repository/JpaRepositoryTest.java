package com.fastcampus.projectboard.repository;

import com.fastcampus.projectboard.config.JpaConfig;
import com.fastcampus.projectboard.createInstances;
import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.ArticleComment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("testdb")
@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {
    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    public JpaRepositoryTest(@Autowired ArticleRepository articleRepository,
                             @Autowired ArticleCommentRepository articleCommentRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @DisplayName("select 테스트")
    @Test
    void given_whenSelecting_thenWorksFine() {
        // Given

        // When
        List<Article> articles = articleRepository.findAll();
        // Then
        assertThat(articles)
                .isNotNull()
                .hasSize(123);
    }

    @DisplayName("insert 테스트")
    @Test
    void given_whenInserting_thenWorksFine() {
        // Given
        long previousCount = articleRepository.count();
        Article article = createInstances.createArticle();

        // When
        articleRepository.save(article);
        // Then
        assertThat(articleRepository.count())
                .isEqualTo(previousCount + 1);
    }

    @DisplayName("update 테스트")
    @Test
    void given_whenUpdating_thenWorksFine() {
        // Given
        Article article = articleRepository.findById(1L).orElseThrow();
        String updatedHashtag = "updated hashtag";
        article.setHashtag(updatedHashtag);
        long previousCount = articleRepository.count();

        // When
        Article updatedArticle = articleRepository.save(article);
        articleRepository.flush();

        // Then
        assertThat(updatedArticle)
                .hasFieldOrPropertyWithValue("hashtag", updatedHashtag);
    }

    @DisplayName("delete 테스트")
    @Test
    void given_whenDeleting_thenWorksFine() {
        // Given
        long previousCount = articleRepository.count();
        long previousArticleCommentCount = articleCommentRepository.count();

        Article article = articleRepository.findById(1L).orElseThrow();

        long articleCommentsSize = article.getArticleComments().size();

        // When
        articleRepository.delete(article);
        articleRepository.flush();

        // Then
        assertThat(articleRepository.count())
                .isEqualTo(previousCount - 1);
        assertThat(articleCommentRepository.count())
                .isEqualTo(previousArticleCommentCount - articleCommentsSize);
    }
}
