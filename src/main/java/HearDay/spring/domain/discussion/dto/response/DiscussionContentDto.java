package HearDay.spring.domain.discussion.dto.response;

import HearDay.spring.common.enums.DiscussionRoleEnum;

import java.util.List;

public record DiscussionContentDto(
        List<Content> discussionsList
) {
    public record Content(
            DiscussionRoleEnum role,
            String content
    ) {}
}
