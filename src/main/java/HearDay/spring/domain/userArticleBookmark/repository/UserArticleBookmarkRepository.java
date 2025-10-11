package HearDay.spring.domain.userArticleBookmark.repository;

import HearDay.spring.domain.userArticleBookmark.entity.UserArticleBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserArticleBookmarkRepository
        extends JpaRepository<UserArticleBookmark, Long>, UserArticleBookmarkRepositoryCustom {}
