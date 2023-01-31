package com.fastcampus.projectboard.service;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.ArticleComment;
import com.fastcampus.projectboard.dto.ArticleCommentDto;
import com.fastcampus.projectboard.repository.ArticleCommentRepository;
import com.fastcampus.projectboard.repository.ArticleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class ArticleCommentService {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    @Transactional(readOnly = true)
    public List<ArticleCommentDto> searchArticleComments(Long articleId) {
        return articleCommentRepository.findByArticle_Id(articleId).stream()
                .map(ArticleCommentDto::from)
                .toList();
    }

    public void saveArticleComment(ArticleCommentDto articleCommentDto) {
        try {
            Article article = articleRepository.getReferenceById(articleCommentDto.articleId());
            ArticleComment articleComment = articleCommentDto.toEntity(article);
            articleCommentRepository.save(articleComment);
        } catch (EntityNotFoundException e) {
            log.warn("댓글 저장 실패. 댓글의 게시글을 찾을 수 없습니다 - dto : {}", articleCommentDto);
        }
    }

    public void updateArticleComment(ArticleCommentDto articleCommentDto) {
        try {
            ArticleComment articleComment = articleCommentRepository.getReferenceById(articleCommentDto.id());
            if(articleCommentDto.content() != null) articleComment.setContent(articleCommentDto.content());
        } catch (EntityNotFoundException e) {
            log.warn("댓글이 존재하지 않습니다 - dto : {}", articleCommentDto);
        }
    }

    public void deleteArticleComment(Long articleCommentId) {
        try {
            articleCommentRepository.deleteById(articleCommentId);
        } catch (EntityNotFoundException e) {
            log.warn("댓글이 존재하지 않습니다 - articleCommentId : {}", articleCommentId);
        }
    }

}
