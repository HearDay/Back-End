package HearDay.spring.domain.article.controller;

import HearDay.spring.common.dto.response.CommonApiResponse;
import HearDay.spring.domain.article.dto.ArticleResponseDto;
import HearDay.spring.domain.article.dto.ArticleSearchDto;
import HearDay.spring.domain.article.service.ArticleService;
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
    public ResponseEntity<CommonApiResponse<List<ArticleResponseDto>>> getAllArticles(
            @RequestBody ArticleSearchDto search, @PageableDefault(size = 10) Pageable page) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonApiResponse.success(articleService.searchArticles(search, page)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonApiResponse<ArticleResponseDto>> getArticle(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonApiResponse.success(articleService.getArticle(id)));
    }
}
