package HearDay.spring.domain.articlequiz.controller;

import HearDay.spring.common.dto.response.CommonApiResponse;
import HearDay.spring.domain.articlequiz.dto.ArticleQuizDto;
import HearDay.spring.domain.articlequiz.service.ArticleQuizService;
import HearDay.spring.domain.user.entity.User;
import HearDay.spring.global.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "ArticleQuiz", description = "기사 퀴즈 API")
@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class ArticleQuizController {

    private final ArticleQuizService articleQuizService;

    @GetMapping("/article/{articleId}")
    @Operation(summary = "기사 퀴즈 조회", description = "특정 기사의 퀴즈를 조회합니다. 유저가 이미 풀었는지 여부도 함께 반환됩니다.")
    public ResponseEntity<CommonApiResponse<ArticleQuizDto>> getArticleQuiz(
            @AuthUser User user,
            @PathVariable Long articleId) {
        ArticleQuizDto quiz = articleQuizService.getArticleQuiz(articleId, user.getId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonApiResponse.success(quiz));
    }

    @PostMapping("/{quizId}/solve")
    @Operation(summary = "퀴즈 풀이 제출", description = "퀴즈를 풀었음을 기록합니다. 프론트에서 정답을 맞췄을 때만 호출합니다.")
    public ResponseEntity<CommonApiResponse<Void>> submitQuizAnswer(
            @AuthUser User user,
            @PathVariable Long quizId) {
        articleQuizService.submitQuizAnswer(quizId, user);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonApiResponse.success("퀴즈 풀이가 제출되었습니다.", null));
    }
}
