package HearDay.spring.domain.article.repository;

import HearDay.spring.domain.article.dto.ArticleSearchDto;
import HearDay.spring.domain.article.entity.Article;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface ArticleRepositoryCustom {
    List<Article> searchArticles(ArticleSearchDto searchDto, Pageable pageable);

    Optional<Article> findByIdWithDetail(Long id);
}
