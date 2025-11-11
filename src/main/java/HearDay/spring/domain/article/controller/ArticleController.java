package HearDay.spring.domain.article.controller;

import HearDay.spring.common.dto.response.CommonApiResponse;
import HearDay.spring.domain.article.dto.ArticleResponseDto;
import HearDay.spring.domain.article.dto.ArticleSearchDto;
import HearDay.spring.domain.article.dto.RecommendResponseDto;
import HearDay.spring.domain.article.service.ArticleService;
import HearDay.spring.domain.user.entity.User;
import HearDay.spring.global.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    @Operation(summary = "글 검색 및 조회", description = "글을 검색하고 페이징하여 조회합니다")
    public ResponseEntity<CommonApiResponse<List<ArticleResponseDto>>> getAllArticles(
            @RequestBody(required = false) ArticleSearchDto search, @PageableDefault(size = 10) Pageable page) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonApiResponse.success(articleService.searchArticles(search, page)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "글 세부정보 조회", description = "특정 글의 세부정보를 조회합니다. 로그인한 사용자의 경우 최근 본 게시글에 자동 추가됩니다")
    public ResponseEntity<CommonApiResponse<ArticleResponseDto>> getArticle(
            @AuthUser User user,  // null 가능 (비로그인 사용자)
            @PathVariable Long id) {
        
        ArticleResponseDto article = articleService.getArticle(
            user != null ? user.getId() : null, 
            id
        );
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonApiResponse.success(article));
    }

//    @GetMapping("/category")
//    @Operation(summary = "카테고리별 뉴스 추천")
//    public ResponseEntity<CommonApiResponse<List<RecommendResponseDto>>>
}
