package HearDay.spring.domain.user.dto.response;

import HearDay.spring.domain.article.entity.Article;
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
    ) {
        public static ArticleDto from(Article article) {
            return new ArticleDto(
                    article.getId(),
                    article.getTitle(),
                    article.getOriginLink(),
                    article.getImageUrl()
            );
        }

    }
}
