package HearDay.spring.domain.article.dto;

import HearDay.spring.common.enums.CategoryEnum;
import java.util.Collection;

public record ArticleSearchDto(Collection<CategoryEnum> categories, String title) {}
