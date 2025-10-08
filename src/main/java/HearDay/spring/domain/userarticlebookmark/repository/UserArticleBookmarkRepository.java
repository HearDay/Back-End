package HearDay.spring.domain.userarticlebookmark.repository;

import HearDay.spring.domain.userarticlebookmark.entity.UserArticleBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserArticleBookmarkRepository
        extends JpaRepository<UserArticleBookmark, Long>, UserArticleBookmarkRepositoryCustom {}
