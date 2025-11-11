package HearDay.spring.domain.user.service;

import HearDay.spring.domain.article.entity.Article;
import HearDay.spring.domain.article.repository.ArticleRepository;
import HearDay.spring.domain.user.dto.response.HomeResponseDto;
import HearDay.spring.domain.user.entity.User;
import HearDay.spring.domain.user.exception.UserException;
import HearDay.spring.domain.user.repository.UserRepository;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final WebClient webClient;

    @Value("${ai.api.url}")
    private String aiUrl;

    @Override
    public void checkId(String userLoginId) {
//        if (userRepository.findByLoginId(userLoginId).isPresent()) {
//            throw new UserException.UserLoginIdAlreadyExistException(userLoginId);
//        }
    }

    @Override
    public User getUserEntity(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserException.UserNotFoundException(userId));
    }

    @Override
    public HomeResponseDto getHomeInformation(User user) {
        Optional<Article> recentUpdatedTime = articleRepository.findTopByOrderByCreatedAtDesc();

        String formattedDate = recentUpdatedTime
                .map(article -> article.getCreatedAt().format(
                        DateTimeFormatter.ofPattern("M월 d일 HH:mm", Locale.KOREAN)
                ))
                .orElse("데이터 없음");

        List<HomeResponseDto.ArticleDto> recommendedArticles = fetchRecommendedArticlesFromAiServer(user.getId());

        return new HomeResponseDto(
                user.getLevel(),
                user.getNickname(),
                formattedDate,
                recommendedArticles
        );
    }

    private List<HomeResponseDto.ArticleDto> fetchRecommendedArticlesFromAiServer(Long userId) {
        try {
            List<AiArticle> aiArticles = webClient.get()
                    .uri(aiUrl + "/users/{userId}/recommendations", userId)
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::is5xxServerError,
                            clientResponse -> {
                                log.error("AI 서버 오류 (userId: {}): {}", userId, clientResponse.statusCode());
                                return Mono.error(new RuntimeException("AI 서버에서 오류가 발생했습니다."));
                            }
                    )
                    .bodyToMono(new ParameterizedTypeReference<List<AiArticle>>() {})
                    .blockOptional()
                    .orElse(List.of());

            return aiArticles.stream()
                    .map(a -> new HomeResponseDto.ArticleDto(
                            a.getId(),
                            a.getTitle(),
                            a.getOriginLink(),
                            a.getImageUrl()
                    ))
                    .toList();

        } catch (Exception e) {
            log.error("AI 서버 통신 중 예외 발생 (userId: {}): {}", userId, e.getMessage());
            return List.of();
        }
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    private static class AiArticle {
        private Long id;
        private String title;
        private String originLink;
        private String imageUrl;

        public Long getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getOriginLink() {
            return originLink;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }
}
