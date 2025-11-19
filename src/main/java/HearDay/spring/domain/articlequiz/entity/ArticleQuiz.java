package HearDay.spring.domain.articlequiz.entity;

import HearDay.spring.common.entity.BaseEntity;
import HearDay.spring.domain.article.entity.Article;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "article_quiz")
public class ArticleQuiz extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false, unique = true)
    private Article article;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String option1;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String option2;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String option3;

    @Column(nullable = false)
    private Integer correctAnswer; // 1, 2, 3 중 정답 번호

    @Column(nullable = false, columnDefinition = "TEXT")
    private String explanation; // 정답에 대한 설명
}
