package HearDay.spring.domain.articlequiz.dto;

import HearDay.spring.domain.articlequiz.entity.ArticleQuiz;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "기사 퀴즈 DTO")
public record ArticleQuizDto(
        @Schema(description = "퀴즈 ID", example = "1") Long id,
        @Schema(description = "퀴즈 문제", example = "KT 차기 CEO 최종 후보는 언제 선정할 예정인가요?") String question,
        @Schema(description = "선택지 1", example = "내년 3월에 선정할 예정이다.") String option1,
        @Schema(description = "선택지 2", example = "올해 연내 선정할 예정이다.") String option2,
        @Schema(description = "선택지 3", example = "주주총회에서 선정할 예정이다.") String option3,
        @Schema(description = "정답 번호 (1, 2, 3 중 하나)", example = "1") Integer correctAnswer,
        @Schema(description = "풀었는지 여부", example = "true") Boolean isSolved
) {
    public static ArticleQuizDto from(ArticleQuiz quiz, Boolean isSolved) {
        return new ArticleQuizDto(
                quiz.getId(),
                quiz.getQuestion(),
                quiz.getOption1(),
                quiz.getOption2(),
                quiz.getOption3(),
                quiz.getCorrectAnswer(),
                isSolved
        );
    }
}
