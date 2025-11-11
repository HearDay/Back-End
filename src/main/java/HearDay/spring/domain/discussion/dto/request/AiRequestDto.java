package HearDay.spring.domain.discussion.dto.request;

import HearDay.spring.common.enums.AiChatLevelEnum;
import HearDay.spring.common.enums.AiChatModeEnum;
import HearDay.spring.common.enums.DiscussionRoleEnum;

import java.util.List;

public record AiRequestDto(
        String user_id,
        String session_id,
        String content,
        String message,
        AiChatModeEnum mode,
        AiChatLevelEnum level
) {
}