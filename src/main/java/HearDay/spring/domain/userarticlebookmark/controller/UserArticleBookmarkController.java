package HearDay.spring.domain.userarticlebookmark.controller;

import HearDay.spring.common.dto.response.CommonApiResponse;
import HearDay.spring.domain.article.dto.ArticleResponseDto;
import HearDay.spring.domain.userarticlebookmark.service.UserArticleBookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Article Bookmark", description = "게시글 북마크 API")
@RestController
@RequestMapping("/api/article-bookmarks")
@RequiredArgsConstructor
public class UserArticleBookmarkController {
    private final UserArticleBookmarkService userArticleBookmarkService;

    @GetMapping
    @Operation(summary = "내 북마크 목록 조회", description = "사용자가 북마크한 게시글 목록을 페이징하여 조회합니다")
    public ResponseEntity<CommonApiResponse<List<ArticleResponseDto>>> getMyBookmarks(
            @PageableDefault(size = 10) Pageable page) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        CommonApiResponse.success(
                                userArticleBookmarkService.getBookmarkArticles(page)));
    }

    @PostMapping("/{articleId}")
    @Operation(summary = "북마크 추가", description = "특정 게시글을 북마크에 추가합니다")
    public ResponseEntity<CommonApiResponse<Boolean>> addBookmark(
            @Parameter(description = "게시글 ID", example = "1", required = true) @PathVariable
                    Long articleId) {
        userArticleBookmarkService.addBookmark(articleId);
        return ResponseEntity.status(HttpStatus.OK).body(CommonApiResponse.success(null));
    }

    @DeleteMapping("/{articleId}")
    @Operation(summary = "북마크 삭제", description = "특정 게시글의 북마크를 삭제합니다")
    public ResponseEntity<CommonApiResponse<Void>> removeBookmark(@PathVariable Long articleId) {
        userArticleBookmarkService.removeBookmark(articleId);
        return ResponseEntity.status(HttpStatus.OK).body(CommonApiResponse.success(null));
    }
}
