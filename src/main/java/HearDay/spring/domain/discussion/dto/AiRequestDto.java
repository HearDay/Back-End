package HearDay.spring.domain.discussion.dto;

import HearDay.spring.common.enums.DiscussionRoleEnum;

import java.util.List;

public record AiRequestDto(
        Long discussionId,
        String nickname,
        String articleTitle,
        String content,
        List<Message> previousMessages,
        String message
) {
    public record Message(
            DiscussionRoleEnum role,    // "user" 또는 "ai"
            String content
    ) {}
}