package HearDay.spring.domain.article.repository;

import HearDay.spring.common.enums.CategoryEnum;
import HearDay.spring.domain.article.dto.ArticleSearchDto;
import HearDay.spring.domain.article.entity.Article;
import HearDay.spring.domain.article.entity.QArticle;
import HearDay.spring.domain.article.entity.QArticleDetail;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Article> searchArticles(ArticleSearchDto searchDto, Pageable pageable) {
        QArticle article = QArticle.article;
        JPAQuery<Article> defaultQuery = queryFactory.selectFrom(article);

        if (searchDto != null) {
            defaultQuery.where(
                    titleContains(searchDto.title()), categoryIn(searchDto.categories()));
        }

        return defaultQuery
                .orderBy(article.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public Optional<Article> findByIdWithDetail(Long id) {
        QArticle article = QArticle.article;
        QArticleDetail detail = QArticleDetail.articleDetail;

        Article result =
                queryFactory
                        .selectFrom(article)
                        .leftJoin(article.articleDetail, detail)
                        .fetchJoin()
                        .where(article.id.eq(id))
                        .fetchOne();

        return Optional.ofNullable(result);
    }

    private BooleanExpression titleContains(String title) {
        return title != null ? QArticle.article.title.contains(title) : null;
    }

    private BooleanExpression categoryIn(Collection<CategoryEnum> categories) {
        return categories != null && !categories.isEmpty()
                ? QArticle.article.articleCategory.in(categories)
                : null;
    }
}
