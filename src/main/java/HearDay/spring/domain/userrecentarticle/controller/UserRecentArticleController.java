package HearDay.spring.domain.userrecentarticle.controller;

import HearDay.spring.common.dto.response.CommonApiResponse;
import HearDay.spring.domain.article.dto.ArticleResponseDto;
import HearDay.spring.domain.user.entity.User;
import HearDay.spring.domain.userrecentarticle.service.UserRecentArticleService;
import HearDay.spring.global.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Recent Article", description = "최근 본 게시글 API")
@RestController
@RequestMapping("/api/recent-articles")
@RequiredArgsConstructor
public class UserRecentArticleController {
    
    private final UserRecentArticleService recentArticleService;

    @GetMapping
    @Operation(summary = "최근 본 게시글 목록 조회", description = "사용자가 최근에 본 게시글 목록을 조회합니다 (최대 20개)")
    public ResponseEntity<CommonApiResponse<List<ArticleResponseDto>>> getRecentArticles(
            @AuthUser User user
    ) {
        List<ArticleResponseDto> recentArticles = recentArticleService.getRecentArticles(user.getId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonApiResponse.success(recentArticles));
    }

    @DeleteMapping
    @Operation(summary = "최근 본 게시글 전체 삭제", description = "사용자의 최근 본 게시글 기록을 모두 삭제합니다")
    public ResponseEntity<Void> clearRecentArticles(@AuthUser User user) {
        recentArticleService.clearRecentArticles(user.getId());
        return ResponseEntity.noContent().build();
    }
}
