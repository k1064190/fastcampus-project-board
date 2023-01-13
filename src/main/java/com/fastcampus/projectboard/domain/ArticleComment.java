package com.fastcampus.projectboard.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "content"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class ArticleComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 댓글 번호

    @Setter @ManyToOne(optional = false) private Article article; // 댓글이 달린 게시글
    @Setter @Column(nullable = false, length = 500) private String content; // 댓글 내용

    @CreatedDate @Column(nullable = false) private LocalDateTime createdAt; // 댓글 작성일
    @CreatedBy @Column(nullable = false) private String createdBy; // 댓글 작성자
    @LastModifiedDate @Column(nullable = false) private LocalDateTime modifiedAt; // 댓글 수정일
    @LastModifiedBy @Column(nullable = false) private String modifiedBy; // 댓글 수정자

    protected ArticleComment() {}

    public static ArticleComment of(Article article, String content) {
        return new ArticleComment(article, content);
    }
    private ArticleComment(Article article, String content) {
        this.article = article;
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleComment that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
