package HearDay.spring.domain.article.service;

import HearDay.spring.common.enums.AgeGroup;
import HearDay.spring.common.enums.Gender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleViewCountService {

    private final RedisTemplate<String, String> redisTemplate;
    
    private static final String VIEW_COUNT_KEY_PREFIX = "article:view:";
    private static final String RANKING_KEY_PREFIX = "article:ranking:";
    private static final String USER_VIEW_KEY_PREFIX = "user:viewed:";
    private static final Duration TTL = Duration.ofHours(24);
    private static final int TOP_N = 5;

    public void incrementViewCount(Long userId, Long articleId, AgeGroup ageGroup, Gender gender) {
        if (ageGroup == null || gender == null) {
            return;
        }
        
        String userViewKey = generateUserViewKey(userId, articleId);
        Boolean alreadyViewed = redisTemplate.hasKey(userViewKey);
        
        if (alreadyViewed) {
            return;
        }
        
        redisTemplate.opsForValue().set(userViewKey, "1");
        redisTemplate.expire(userViewKey, TTL);
        
        String rankingKey = generateRankingKey(ageGroup, gender);
        String viewKey = generateViewKey(articleId, ageGroup, gender);
        
        Long currentCount = redisTemplate.opsForValue().increment(viewKey);
        redisTemplate.expire(viewKey, TTL);
        
        redisTemplate.opsForZSet().add(rankingKey, articleId.toString(), currentCount.doubleValue());
    }

    public List<Long> getTopArticles(AgeGroup ageGroup, Gender gender) {
        if (ageGroup == null || gender == null) {
            return List.of();
        }
        
        String rankingKey = generateRankingKey(ageGroup, gender);
        
        cleanExpiredArticles(rankingKey, ageGroup, gender);
        
        Set<String> topArticleIds = redisTemplate.opsForZSet()
                .reverseRange(rankingKey, 0, TOP_N - 1);
        
        if (topArticleIds == null || topArticleIds.isEmpty()) {
            return List.of();
        }
        
        return topArticleIds.stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    private void cleanExpiredArticles(String rankingKey, AgeGroup ageGroup, Gender gender) {
        Set<String> allArticleIds = redisTemplate.opsForZSet().range(rankingKey, 0, -1);
        
        if (allArticleIds == null || allArticleIds.isEmpty()) {
            return;
        }
        
        for (String articleId : allArticleIds) {
            String viewKey = generateViewKey(Long.parseLong(articleId), ageGroup, gender);
            Boolean exists = redisTemplate.hasKey(viewKey);
            
            if (!exists) {
                redisTemplate.opsForZSet().remove(rankingKey, articleId);
            }
        }
    }

    private String generateViewKey(Long articleId, AgeGroup ageGroup, Gender gender) {
        return VIEW_COUNT_KEY_PREFIX + articleId + ":" + ageGroup.name() + ":" + gender.name();
    }

    private String generateRankingKey(AgeGroup ageGroup, Gender gender) {
        return RANKING_KEY_PREFIX + ageGroup.name() + ":" + gender.name();
    }

    private String generateUserViewKey(Long userId, Long articleId) {
        return USER_VIEW_KEY_PREFIX + userId + ":" + articleId;
    }
}
