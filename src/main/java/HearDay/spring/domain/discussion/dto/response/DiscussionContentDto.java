package HearDay.spring.domain.discussion.dto.response;

import HearDay.spring.common.enums.DiscussionRoleEnum;

import java.util.List;

public record DiscussionContentDto(
        List<Content> contentList
) {
    public record Content(
            Long contentId,
            DiscussionRoleEnum role,
            String content
    ) {}
}
