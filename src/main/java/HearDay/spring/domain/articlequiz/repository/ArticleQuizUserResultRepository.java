package HearDay.spring.domain.articlequiz.repository;

import HearDay.spring.domain.articlequiz.entity.ArticleQuizUserResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleQuizUserResultRepository extends JpaRepository<ArticleQuizUserResult, Long> {
    
    Optional<ArticleQuizUserResult> findByArticleQuizIdAndUserId(Long articleQuizId, Long userId);
    
    @Query("SELECT r FROM ArticleQuizUserResult r WHERE r.articleQuiz.id IN :quizIds AND r.user.id = :userId")
    List<ArticleQuizUserResult> findByQuizIdsAndUserId(@Param("quizIds") List<Long> quizIds, @Param("userId") Long userId);
}
