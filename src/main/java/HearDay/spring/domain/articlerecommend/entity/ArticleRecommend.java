package HearDay.spring.domain.articlerecommend.entity;

import HearDay.spring.common.entity.BaseEntity;
import HearDay.spring.common.enums.RecommendEnum;
import HearDay.spring.domain.article.entity.Article;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class ArticleRecommend extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @CollectionTable(
            name = "article_recommend_keywords", // 별도 테이블 생성
            joinColumns = @JoinColumn(name = "article_recommend_id") // Article 엔티티와 매핑
    )
    @Column(name = "keyword")
    private List<String> keywords;

    @ElementCollection
    @CollectionTable(
            name = "article_recommend_vector", // 별도 테이블 생성
            joinColumns = @JoinColumn(name = "article_recommend_id") // Article 엔티티와 매핑
    )
    @Column(name = "sbertVector", columnDefinition = "TEXT")
    private List<String> sbertVector;

    @Enumerated(EnumType.STRING)
    private RecommendEnum status;

    @Column(name = "bias_label", columnDefinition = "TEXT")
    private String biasLabel;

    @Column(name = "bias_score", columnDefinition = "FLOAT")
    private Float biasScore;

    @OneToOne(mappedBy = "articleRecommend", fetch = FetchType.LAZY)
    private Article article;

    @Column(name = "article_cluster_id")
    private Integer articleClusterId;
}
