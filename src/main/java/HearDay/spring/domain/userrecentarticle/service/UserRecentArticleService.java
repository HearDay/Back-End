package HearDay.spring.domain.userrecentarticle.service;

import HearDay.spring.domain.article.dto.ArticleResponseDto;
import HearDay.spring.domain.article.entity.Article;
import HearDay.spring.domain.user.entity.User;
import HearDay.spring.domain.user.repository.UserRepository;
import HearDay.spring.domain.userrecentarticle.entity.UserRecentArticle;
import HearDay.spring.domain.userrecentarticle.repository.UserRecentArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserRecentArticleService {
    
    private static final int MAX_RECENT_ARTICLES = 20;
    
    private final UserRecentArticleRepository recentArticleRepository;
    private final UserRepository userRepository;

    /**
     * 최근 본 게시글 목록 조회 (최대 20개, 최신순)
     */
    public List<ArticleResponseDto> getRecentArticles(Long userId) {
        List<UserRecentArticle> recentArticles = recentArticleRepository
                .findOldestByUserId(userId, MAX_RECENT_ARTICLES);
        
        // 최신순으로 정렬하여 반환
        return recentArticles.stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .map(recent -> ArticleResponseDto.from(recent.getArticle()))
                .toList();
    }

    /**
     * 최근 본 게시글에 추가
     * - 이미 존재하면 조회 시간 갱신 (삭제 후 재생성)
     * - 20개 초과 시 가장 오래된 것부터 삭제
     */
    @Transactional
    public void addRecentArticle(Long userId, Article article) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Optional<UserRecentArticle> existingRecord = recentArticleRepository
                .findByUserIdAndArticleId(userId, article.getId());
        
        existingRecord.ifPresent(recentArticleRepository::delete);

        long currentCount = recentArticleRepository.countByUserId(userId);
        
        if (currentCount >= MAX_RECENT_ARTICLES) {
            int deleteCount = (int) (currentCount - MAX_RECENT_ARTICLES + 1);
            List<UserRecentArticle> oldestRecords = recentArticleRepository
                    .findOldestByUserId(userId, deleteCount);
            
            recentArticleRepository.deleteAll(oldestRecords);
        }

        // 4. 새로운 기록 추가
        UserRecentArticle newRecord = UserRecentArticle.builder()
                .user(user)
                .article(article)
                .build();

        recentArticleRepository.save(newRecord);
        log.info("Added recent article {} for user {}", article.getId(), userId);
    }

    /**
     * 최근 본 게시글 전체 삭제
     */
    @Transactional
    public void clearRecentArticles(Long userId) {
        List<UserRecentArticle> recentArticles = recentArticleRepository
                .findOldestByUserId(userId, MAX_RECENT_ARTICLES);
        
        recentArticleRepository.deleteAll(recentArticles);
        log.info("Cleared all recent articles for user {}", userId);
    }
}
