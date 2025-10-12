package HearDay.spring.domain.userarticlebookmark.repository;

import HearDay.spring.domain.article.entity.QArticle;
import HearDay.spring.domain.userarticlebookmark.entity.QUserArticleBookmark;
import HearDay.spring.domain.userarticlebookmark.entity.UserArticleBookmark;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class UserArticleBookmarkRepositoryImpl implements UserArticleBookmarkRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByUserIdAndArticleId(Long userId, Long articleId) {
        QUserArticleBookmark userArticleBookmark = QUserArticleBookmark.userArticleBookmark;

        Integer fetchOne =
                queryFactory
                        .selectOne()
                        .from(userArticleBookmark)
                        .where(
                                userArticleBookmark.user.id.eq(userId),
                                userArticleBookmark.article.id.eq(articleId))
                        .fetchFirst();

        return fetchOne != null;
    }

    @Override
    public Optional<UserArticleBookmark> findByUserIdAndArticleId(Long userId, Long articleId) {
        QUserArticleBookmark userArticleBookmark = QUserArticleBookmark.userArticleBookmark;

        UserArticleBookmark fetchOne =
                queryFactory
                        .selectFrom(userArticleBookmark)
                        .where(
                                userArticleBookmark.user.id.eq(userId),
                                userArticleBookmark.article.id.eq(articleId))
                        .fetchOne();

        return Optional.ofNullable(fetchOne);
    }

    @Override
    public List<UserArticleBookmark> findByUserIdWithArticle(Long userId, Pageable pageable) {
        QUserArticleBookmark userArticleBookmark = QUserArticleBookmark.userArticleBookmark;
        QArticle article = QArticle.article;

        List<UserArticleBookmark> articles =
                queryFactory
                        .selectFrom(userArticleBookmark)
                        .join(userArticleBookmark.article, article)
                        .fetchJoin()
                        .where(userArticleBookmark.user.id.eq(userId))
                        .orderBy(userArticleBookmark.createdAt.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        return articles;
    }
}
