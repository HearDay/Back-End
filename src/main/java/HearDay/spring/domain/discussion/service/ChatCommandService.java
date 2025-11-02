package HearDay.spring.domain.discussion.service;

import HearDay.spring.domain.discussion.dto.ChatResponseDto;
import HearDay.spring.domain.user.entity.User;
import reactor.core.publisher.Mono;

public interface ChatCommandService {
    Mono<ChatResponseDto> getAiReply(String request, Long articleId, Long discussionId, User user);
}
