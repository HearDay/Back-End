package HearDay.spring.domain.article.service;

import HearDay.spring.common.enums.AgeGroup;
import HearDay.spring.common.enums.CategoryEnum;
import HearDay.spring.domain.article.dto.ArticleResponseDto;
import HearDay.spring.domain.article.dto.ArticleSearchDto;
import HearDay.spring.domain.article.dto.RecommendResponseDto;
import HearDay.spring.domain.article.entity.Article;
import HearDay.spring.domain.article.exception.ArticleException;
import HearDay.spring.domain.article.repository.ArticleRepository;
import HearDay.spring.domain.user.entity.User;
import HearDay.spring.domain.userrecentarticle.service.UserRecentArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRecentArticleService recentArticleService;
    private final ArticleViewCountService articleViewCountService;
    private final WebClient webClient;

    @Value("${ai.api.url}")
    private String aiUrl;

    public List<ArticleResponseDto> searchArticles(ArticleSearchDto searchDto, Pageable pageable) {
        return articleRepository.searchArticles(searchDto, pageable).stream()
                .map(ArticleResponseDto::from)
                .toList();
    }

    @Transactional
    public ArticleResponseDto getArticle(User user, Long id) {
        Article article =
                articleRepository
                        .findByIdWithDetail(id)
                        .orElseThrow(() -> new ArticleException.ArticleNotFoundException(id));

        if (user != null) {
            recentArticleService.addRecentArticle(user.getId(), article);
            AgeGroup ageGroup = AgeGroup.fromAge(user.getAge());
            articleViewCountService.incrementViewCount(user.getId(), article.getId(), ageGroup, user.getGender());
        }

        return ArticleResponseDto.fromWithDetail(article);
    }

    public Article getArticleEntity(Long id) {
        return articleRepository
                .findByIdWithDetail(id)
                .orElseThrow(() -> new ArticleException.ArticleNotFoundException(id));
    }

    public List<RecommendResponseDto> getCategoryRecommend(User user, CategoryEnum category) {
        try {
            return webClient.get()
                    .uri(aiUrl + "/users/{userId}/recommendations/category/{categoryName}", user.getId(), category)
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::is5xxServerError,
                            clientResponse -> {
                                log.error("AI 서버 오류 (userId: {}, category: {}): {}", user.getId(), category, clientResponse.statusCode());
                                return Mono.error(new RuntimeException("AI 서버에서 오류가 발생했습니다."));
                            }
                    )
                    .bodyToMono(new ParameterizedTypeReference<List<RecommendResponseDto>>() {
                    })
                    .blockOptional()
                    .orElse(List.of());
        } catch (Exception e) {
            log.error("AI 서버 통신 중 예외 발생 (userId: {}, category: {}): {}", user.getId(), category, e.getMessage());
            return List.of();
        }
    }
    
    public List<ArticleResponseDto> getTopArticlesByDemographic(User user) {
        AgeGroup ageGroup = AgeGroup.fromAge(user.getAge());
        
        if (ageGroup == null || user.getGender() == null) {
            log.warn("User age or gender is not set. Cannot get top articles for userId: {}", user.getId());
            return List.of();
        }
        
        List<Long> topArticleIds = articleViewCountService.getTopArticles(ageGroup, user.getGender());
        
        if (topArticleIds.isEmpty()) {
            return List.of();
        }
        
        // Article ID 목록으로 실제 Article 조회
        return articleRepository.findAllById(topArticleIds).stream()
                .map(ArticleResponseDto::from)
                .toList();
    }
}
