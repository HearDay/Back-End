package HearDay.spring.domain.userrecentarticle.repository;

import HearDay.spring.domain.userrecentarticle.entity.UserRecentArticle;

import java.util.List;
import java.util.Optional;

public interface UserRecentArticleRepositoryCustom {
    
    Optional<UserRecentArticle> findByUserIdAndArticleId(Long userId, Long articleId);
    
    long countByUserId(Long userId);

    List<UserRecentArticle> findOldestByUserId(Long userId, int limit);
}
