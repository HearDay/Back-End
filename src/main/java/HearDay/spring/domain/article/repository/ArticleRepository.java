package HearDay.spring.domain.article.repository;

import HearDay.spring.domain.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {
    Optional<Article> findTopByOrderByCreatedAtDesc();
}
