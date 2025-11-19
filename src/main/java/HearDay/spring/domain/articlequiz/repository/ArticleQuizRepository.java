package HearDay.spring.domain.articlequiz.repository;

import HearDay.spring.domain.articlequiz.entity.ArticleQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleQuizRepository extends JpaRepository<ArticleQuiz, Long> {
    Optional<ArticleQuiz> findByArticleId(Long articleId);
}
