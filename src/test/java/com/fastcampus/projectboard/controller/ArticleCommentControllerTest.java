package com.fastcampus.projectboard.controller;

import com.fastcampus.projectboard.config.SecurityConfig;
import com.fastcampus.projectboard.dto.ArticleCommentDto;
import com.fastcampus.projectboard.request.ArticleCommentRequest;
import com.fastcampus.projectboard.service.ArticleCommentService;
import com.fastcampus.projectboard.util.CreateInstance;
import com.fastcampus.projectboard.util.FormDataEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.text.Normalizer;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 댓글")
@Import({SecurityConfig.class, FormDataEncoder.class})
@WebMvcTest(ArticleCommentController.class)
class ArticleCommentControllerTest {

    @MockBean
    private ArticleCommentService articleCommentService;

    private final MockMvc mvc;
    private final FormDataEncoder formDataEncoder;

    public ArticleCommentControllerTest(@Autowired MockMvc mvc, @Autowired FormDataEncoder formDataEncoder) {
        this.mvc = mvc;
        this.formDataEncoder = formDataEncoder;
    }

    @DisplayName("[view][POST] 댓글 등록 - 정상 호출")
    @Test
    void givenArticleCommentInfo_whenRequesting_thenSavesNewArticleComment() throws Exception {
        // Given
        Long articleId = 1L;
        String content = "댓글 내용";
        ArticleCommentRequest articleCommentRequest = ArticleCommentRequest.of(articleId, content);
        willDoNothing().given(articleCommentService).saveArticleComment(any(ArticleCommentDto.class));
        // When & Then
        mvc.perform(post("/comments/new")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(formDataEncoder.encode(articleCommentRequest))
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/articles/" + articleId))
                .andExpect(redirectedUrl("/articles/" + articleId));
        then(articleCommentService).should().saveArticleComment(any(ArticleCommentDto.class));
    }

    @DisplayName("[view][POST] 댓글 삭제 - 정상 호출")
    @Test
    void givenArticleIdAndArticleCommentId_whenRequesting_thenDeletesArticleComment() throws Exception {
        // Given
        Long articleId = 1L;
        Long articleCommentId = 1L;
        willDoNothing().given(articleCommentService).deleteArticleComment(articleCommentId);
        // When & Then
        mvc.perform(post("/comments/" + articleCommentId + "/delete")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(formDataEncoder.encode(Map.of("articleId", articleId)))
                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/articles/" + articleId))
                .andExpect(redirectedUrl("/articles/" + articleId));
        then(articleCommentService).should().deleteArticleComment(articleCommentId);
    }
}
