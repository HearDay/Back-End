package HearDay.spring.domain.article.service;

import HearDay.spring.domain.article.dto.ArticleResponseDto;
import HearDay.spring.domain.article.dto.ArticleSearchDto;
import HearDay.spring.domain.article.entity.Article;
import HearDay.spring.domain.article.exception.ArticleException;
import HearDay.spring.domain.article.repository.ArticleRepository;
import HearDay.spring.domain.userrecentarticle.service.UserRecentArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRecentArticleService recentArticleService;

    public List<ArticleResponseDto> searchArticles(ArticleSearchDto searchDto, Pageable pageable) {
        return articleRepository.searchArticles(searchDto, pageable).stream()
                .map(ArticleResponseDto::from)
                .toList();
    }

    @Transactional
    public ArticleResponseDto getArticle(Long userId, Long id) {
        Article article =
                articleRepository
                        .findByIdWithDetail(id)
                        .orElseThrow(() -> new ArticleException.ArticleNotFoundException(id));

        // 로그인한 사용자만 최근 본 게시글에 추가
        if (userId != null) {
            recentArticleService.addRecentArticle(userId, article);
        }

        return ArticleResponseDto.fromWithDetail(article);
    }

    public Article getArticleEntity(Long id) {
        return articleRepository
                .findByIdWithDetail(id)
                .orElseThrow(() -> new ArticleException.ArticleNotFoundException(id));
    }
}
