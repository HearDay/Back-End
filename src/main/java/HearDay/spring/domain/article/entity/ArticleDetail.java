package HearDay.spring.domain.article.entity;

import HearDay.spring.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ArticleDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String ttsUrl;

    @Column(columnDefinition = "TEXT")
    private String ttsAlignment;

    @OneToOne(mappedBy = "articleDetail", fetch = FetchType.LAZY)
    private Article article;
}
