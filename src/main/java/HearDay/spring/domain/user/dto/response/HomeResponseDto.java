package HearDay.spring.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record HomeResponseDto(
        Integer level,
        String nickname,
        String updateTime,
        List<ArticleDto> recommendedArticles
) {
    public record ArticleDto(
            Long id,
            String title,
            @JsonProperty("origin_link") String origin_link,
            @JsonProperty("image_url") String imageUrl
    ) {}
}
