package HearDay.spring.domain.discussion.service;

import HearDay.spring.common.enums.AiChatLevelEnum;
import HearDay.spring.domain.discussion.dto.request.ChatRequestDto;
import HearDay.spring.domain.discussion.dto.response.ChatResponseDto;
import HearDay.spring.domain.discussion.dto.response.VoiceResponseDto;
import HearDay.spring.domain.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface ChatCommandService {
    ChatResponseDto getAiReply(ChatRequestDto request, Long articleId, Long discussionId, User user);
    VoiceResponseDto getAiVoiceReply(Long discussionId, Long articleId, MultipartFile audioFile, AiChatLevelEnum level, User user);
}
