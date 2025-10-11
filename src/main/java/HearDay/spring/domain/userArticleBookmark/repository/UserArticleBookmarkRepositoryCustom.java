package HearDay.spring.domain.userArticleBookmark.repository;

import HearDay.spring.domain.userArticleBookmark.entity.UserArticleBookmark;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface UserArticleBookmarkRepositoryCustom {
    boolean existsByUserIdAndArticleId(Long userId, Long articleId);

    List<UserArticleBookmark> findByUserIdWithArticle(Long userId, Pageable pageable);

    Optional<UserArticleBookmark> findByUserIdAndArticleId(Long userId, Long articleId);
}
