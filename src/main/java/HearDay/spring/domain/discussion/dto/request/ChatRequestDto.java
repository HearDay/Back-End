package HearDay.spring.domain.discussion.dto.request;

import HearDay.spring.common.enums.AiChatLevelEnum;

public record ChatRequestDto(
        String message,
        AiChatLevelEnum level
) {
}
