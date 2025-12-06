package HearDay.spring.domain.article.dto;

import HearDay.spring.domain.article.entity.Article;
import com.fasterxml.jackson.annotation.JsonProperty;

public record RecommendResponseDto(
        Long id,
        String title,
        @JsonProperty("origin_link") String originLink,
        @JsonProperty("image_url") String imageUrl
        ) {
    public static RecommendResponseDto from(Article article) {
        return new RecommendResponseDto(
                article.getId(),
                article.getTitle(),
                article.getOriginLink(),
                article.getImageUrl()
        );
    }
}
