package HearDay.spring.domain.userrecentarticle.repository;

import HearDay.spring.domain.userrecentarticle.entity.UserRecentArticle;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static HearDay.spring.domain.userrecentarticle.entity.QUserRecentArticle.userRecentArticle;

@RequiredArgsConstructor
public class UserRecentArticleRepositoryImpl implements UserRecentArticleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<UserRecentArticle> findByUserIdAndArticleId(Long userId, Long articleId) {
        UserRecentArticle result = queryFactory
                .selectFrom(userRecentArticle)
                .where(
                    userRecentArticle.user.id.eq(userId),
                    userRecentArticle.article.id.eq(articleId)
                )
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public long countByUserId(Long userId) {
        Long count = queryFactory
                .select(userRecentArticle.count())
                .from(userRecentArticle)
                .where(userRecentArticle.user.id.eq(userId))
                .fetchOne();

        return count != null ? count : 0L;
    }

    @Override
    public List<UserRecentArticle> findOldestByUserId(Long userId, int limit) {
        return queryFactory
                .selectFrom(userRecentArticle)
                .where(userRecentArticle.user.id.eq(userId))
                .orderBy(userRecentArticle.createdAt.asc())  // 오래된 순
                .limit(limit)
                .fetch();
    }
}
