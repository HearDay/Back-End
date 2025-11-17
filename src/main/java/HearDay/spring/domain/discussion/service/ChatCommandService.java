package HearDay.spring.domain.discussion.service;

import HearDay.spring.domain.discussion.dto.request.ChatRequestDto;
import HearDay.spring.domain.discussion.dto.response.ChatResponseDto;
import HearDay.spring.domain.user.entity.User;
import reactor.core.publisher.Mono;

public interface ChatCommandService {
    ChatResponseDto getAiReply(ChatRequestDto request, Long articleId, Long discussionId, User user);
}
