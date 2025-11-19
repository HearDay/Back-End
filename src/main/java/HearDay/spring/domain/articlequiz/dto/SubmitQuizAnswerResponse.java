package HearDay.spring.domain.articlequiz.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "퀴즈 답안 제출 응답 DTO")
public record SubmitQuizAnswerResponse(
        @Schema(description = "정답 설명", example = "올해 연내 선정할 예정입니다.") String explanation
) {
}
