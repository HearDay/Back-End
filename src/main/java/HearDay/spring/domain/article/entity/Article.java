package HearDay.spring.domain.article.entity;

import HearDay.spring.common.entity.BaseEntity;
import HearDay.spring.common.enums.CategoryEnum;
import HearDay.spring.domain.articledetail.entity.ArticleDetail;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originLink;

    @Column(nullable = false)
    private LocalDateTime publishDate;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @ElementCollection(targetClass = CategoryEnum.class)
    @CollectionTable(
            name = "article_category",  // 별도 테이블 생성
            joinColumns = @JoinColumn(name = "article_id") // User 엔티티와 매핑
    )
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private List<CategoryEnum> articleCategory;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArticleDetail> articleDetailList = new ArrayList<>();
}
