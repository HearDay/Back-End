package HearDay.spring.domain.article.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RecommendResponseDto(
        Long id,
        String title,
        @JsonProperty("origin_link") String originLink,
        @JsonProperty("image_url") String imageUrl
        ) {
}
