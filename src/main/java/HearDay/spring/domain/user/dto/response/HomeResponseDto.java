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
            @JsonProperty("originLink") String origin_link,
            @JsonProperty("imageUrl") String image_url
    ) {}
}
