package HearDay.spring.domain.article.controller;

import HearDay.spring.common.dto.response.CommonApiResponse;
import HearDay.spring.domain.article.dto.ArticleResponseDto;
import HearDay.spring.domain.article.dto.ArticleSearchDto;
import HearDay.spring.domain.article.service.ArticleService;
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
    @Operation(summary = "글 세부정보 조회", description = "특정 글의 세부정보를 조회합니다")
    public ResponseEntity<CommonApiResponse<ArticleResponseDto>> getArticle(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonApiResponse.success(articleService.getArticle(id)));
    }
}
