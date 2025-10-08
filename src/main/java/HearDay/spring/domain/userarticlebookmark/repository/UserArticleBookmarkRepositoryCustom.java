package HearDay.spring.domain.userarticlebookmark.repository;

import HearDay.spring.domain.userarticlebookmark.entity.UserArticleBookmark;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface UserArticleBookmarkRepositoryCustom {
    boolean existsByUserIdAndArticleId(Long userId, Long articleId);

    List<UserArticleBookmark> findByUserIdWithArticle(Long userId, Pageable pageable);

    Optional<UserArticleBookmark> findByUserIdAndArticleId(Long userId, Long articleId);
}
