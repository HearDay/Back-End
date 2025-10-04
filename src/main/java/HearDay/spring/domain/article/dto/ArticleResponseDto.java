package HearDay.spring.domain.article.dto;

import HearDay.spring.domain.article.entity.Article;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "게시글 응답 DTO")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ArticleResponseDto(
        @Schema(description = "게시글 ID", example = "1") Long id,
        @Schema(description = "게시글 제목", example = "[Q&AI] 코레일 추석 예매 사이트 먹통...해결책은?") String title,
        @Schema(description = "게시글 요약", example = "사람 예능 영차 공급 부족 전자미 아예 추석 거치 예매 당황이 유난히 차질해졌습니다.")
                String description,
        @Schema(description = "상세정보") ArticleResponseDtoDetail detail,
        @Schema(description = "이미지 URL", example = "https://example.com/image.jpg") String imageUrl,
        @Schema(description = "카테고리", example = "IT") String category,
        @Schema(description = "수정 일시", example = "2025-10-04T15:20:00") LocalDateTime updatedAt) {

    public record ArticleResponseDtoDetail(
            @Schema(
                            description = "게시글 본문",
                            example = "사람 예능 영차 공급 부족 전자미 아예 추석 거치 예매 당황이 유난히 차질해졌습니다.")
                    String content,
            @Schema(description = "ttsUrl", example = "https://example.com/tts.mp3")
                    String ttsUrl) {}

    public static ArticleResponseDto from(Article article) {
        ArticleResponseDtoDetail detail = null;
        if (article.getArticleDetail() != null) {
            detail =
                    new ArticleResponseDtoDetail(
                            article.getArticleDetail().getContent(),
                            article.getArticleDetail().getTtsUrl());
        }

        return new ArticleResponseDto(
                article.getId(),
                article.getTitle(),
                article.getDescription(),
                detail,
                article.getImageUrl(),
                article.getArticleCategory().name(),
                article.getUpdatedAt());
    }
}
