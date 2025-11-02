package HearDay.spring.domain.userrecentarticle.repository;

import HearDay.spring.domain.userrecentarticle.entity.UserRecentArticle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRecentArticleRepository 
    extends JpaRepository<UserRecentArticle, Long>, UserRecentArticleRepositoryCustom {
}
