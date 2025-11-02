package HearDay.spring.domain.discussion.controller;

import HearDay.spring.domain.discussion.dto.ChatResponseDto;
import HearDay.spring.domain.discussion.service.ChatCommandService;
import HearDay.spring.domain.user.entity.User;
import HearDay.spring.global.annotation.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatCommandService chatCommandService;

    @PostMapping
    public Mono<ChatResponseDto> postChat(
            @RequestParam(required = false) Long discussionId,
            @RequestParam Long articleId,
            @RequestBody String request,
            @AuthUser User user
    ) {
        return chatCommandService.getAiReply(request, articleId, discussionId, user);
    }
}
