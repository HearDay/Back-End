package HearDay.spring.domain.article.service;

import HearDay.spring.domain.article.dto.ArticleResponseDto;
import HearDay.spring.domain.article.dto.ArticleSearchDto;
import HearDay.spring.domain.article.entity.Article;
import HearDay.spring.domain.article.exception.ArticleException;
import HearDay.spring.domain.article.repository.ArticleRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;

    public List<ArticleResponseDto> searchArticles(ArticleSearchDto searchDto, Pageable pageable) {
        return articleRepository.searchArticles(searchDto, pageable).stream()
                .map(ArticleResponseDto::from)
                .toList();
    }

    public ArticleResponseDto getArticle(Long id) {
        Article article =
                articleRepository
                        .findByIdWithDetail(id)
                        .orElseThrow(() -> new ArticleException.ArticleNotFoundException(id));

        return ArticleResponseDto.fromWithDetail(article);
    }

    public Article getArticleEntity(Long id) {
        return articleRepository
                .findByIdWithDetail(id)
                .orElseThrow(() -> new ArticleException.ArticleNotFoundException(id));
    }
}
