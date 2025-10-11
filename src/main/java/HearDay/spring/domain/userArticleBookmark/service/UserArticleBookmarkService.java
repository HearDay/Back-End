package HearDay.spring.domain.userArticleBookmark.service;

import HearDay.spring.domain.article.dto.ArticleResponseDto;
import HearDay.spring.domain.article.entity.Article;
import HearDay.spring.domain.article.service.ArticleService;
import HearDay.spring.domain.user.entity.User;
import HearDay.spring.domain.user.service.UserQueryService;
import HearDay.spring.domain.userArticleBookmark.entity.UserArticleBookmark;
import HearDay.spring.domain.userArticleBookmark.exception.UserArticleBookmarkException;
import HearDay.spring.domain.userArticleBookmark.repository.UserArticleBookmarkRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserArticleBookmarkService {

    private final ArticleService articleService;

    private final UserArticleBookmarkRepository articleBookmarkRepository;

    private final UserQueryService userQueryService;

    public List<ArticleResponseDto> getBookmarkArticles(Pageable page) {
        Long userId = 1L; // TODO: 임시 userId, 추후 SecurityContext에서 userId 가져오도록 변경
        return articleBookmarkRepository.findByUserIdWithArticle(userId, page).stream()
                .map(articleBookmark -> ArticleResponseDto.from(articleBookmark.getArticle()))
                .toList();
    }

    @Transactional
    public void addBookmark(Long articleId) {
        Long userId = 1L; // TODO: 임시 userId, 추후 SecurityContext에서 userId 가져오도록 변경

        if (isBookmarked(userId, articleId)) {
            throw new UserArticleBookmarkException.ArticleAlreadyBookmarkException(articleId);
        }

        Article article = articleService.getArticleEntity(articleId);
        User user = userQueryService.getUserEntity(userId);

        UserArticleBookmark bookmark =
                UserArticleBookmark.builder().user(user).article(article).build();

        articleBookmarkRepository.save(bookmark);
    }

    public void removeBookmark(Long articleId) {
        Long userId = 1L; // TODO: 임시 userId, 추후 SecurityContext에서 userId 가져오도록 변경

        UserArticleBookmark bookmark =
                articleBookmarkRepository
                        .findByUserIdAndArticleId(userId, articleId)
                        .orElseThrow(
                                () ->
                                        new UserArticleBookmarkException
                                                .ArticleBookmarkNotExistsException(articleId));
        articleBookmarkRepository.delete(bookmark);
    }

    private boolean isBookmarked(Long userId, Long articleId) {
        return articleBookmarkRepository.existsByUserIdAndArticleId(userId, articleId);
    }
}
