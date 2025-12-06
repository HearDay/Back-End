package HearDay.spring.domain.discussion.service;

import HearDay.spring.common.enums.AiChatLevelEnum;
import HearDay.spring.common.enums.AiChatModeEnum;
import HearDay.spring.common.enums.DiscussionRoleEnum;
import HearDay.spring.domain.article.entity.Article;
import HearDay.spring.domain.article.exception.ArticleException;
import HearDay.spring.domain.article.repository.ArticleRepository;
import HearDay.spring.domain.discussion.dto.request.AiRequestDto;
import HearDay.spring.domain.discussion.dto.request.ChatRequestDto;
import HearDay.spring.domain.discussion.dto.response.ChatResponseDto;
import HearDay.spring.domain.discussion.dto.response.VoiceResponseDto;
import HearDay.spring.domain.discussion.entity.Discussion;
import HearDay.spring.domain.discussion.entity.DiscussionContent;
import HearDay.spring.domain.discussion.exception.DiscussionException;
import HearDay.spring.domain.discussion.repository.DiscussionContentRepository;
import HearDay.spring.domain.discussion.repository.DiscussionRepository;
import HearDay.spring.domain.user.entity.User;
import HearDay.spring.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatCommandServiceImpl implements ChatCommandService {
    private final WebClient webClient;
    private final ArticleRepository articleRepository;
    private final DiscussionContentRepository discussionContentRepository;
    private final DiscussionRepository discussionRepository;
    private final SpeechToTextService sttService;
    private final TextToSpeechService ttsService;
    private final UserRepository userRepository;

    @Value("${ai.api.url}")
    private String aiUrl;

    @Override
    public ChatResponseDto getAiReply(ChatRequestDto request, Long articleId, Long discussionId, User user) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleException.ArticleNotFoundException(articleId));

        // 첫 메세지면 새 토론 생성
        Discussion discussion;
        AiChatModeEnum mode = AiChatModeEnum.FOLLOWUP;

        if (discussionId == null) {
            discussion = discussionRepository.save(
                    Discussion.builder()
                            .article(article)
                            .user(user)
                            .build()
            );

            user.addPoint(10);
            userRepository.save(user);
        } else {
            discussion = discussionRepository.findById(discussionId)
                    .orElseThrow(() -> new DiscussionException.DiscussionNotFoundException(discussionId));
        }

        // 현재 유저 메세지 저장
        discussionContentRepository.save(
                DiscussionContent.builder()
                        .discussion(discussion)
                        .role(DiscussionRoleEnum.USER)
                        .content(request.message())
                        .build()
        );

        // AI 요청 DTO
        AiRequestDto aiRequest = new AiRequestDto(
                user.getId().toString(),
                discussion.getId().toString(),
                article.getDescription(),
                request.message(),
                mode,
                request.level()
        );

        String result = requestAiDiscussion(aiRequest);

        discussionContentRepository.save(
                DiscussionContent.builder()
                        .discussion(discussion)
                        .role(DiscussionRoleEnum.AI)
                        .content(result)
                        .build()
        );

        return new ChatResponseDto(
            result, discussion.getId()
        );
    }

    public String requestAiDiscussion(AiRequestDto requestDto) {
        try {
            Map<String, Object> response = WebClient.builder()
                    .build()
                    .post()
                    .uri(aiUrl + "/feedback/discussion")
                    .bodyValue(requestDto)
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::is5xxServerError,
                            clientResponse -> {
                                log.error("AI 서버 오류 (ai토론 ID: {}): {}",
                                        requestDto.session_id(), clientResponse.statusCode());
                                return Mono.error(new RuntimeException("AI 서버에서 오류가 발생했습니다."));
                            }
                    )
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

            return response != null ? (String) response.get("reply") : null;

        } catch (WebClientResponseException e) {
            log.error("AI 서버 요청 실패 (status={}, body={}): {}",
                    e.getStatusCode(), e.getResponseBodyAsString(), e.getMessage());
            throw new RuntimeException("AI 서버 요청 중 오류가 발생했습니다.");
        } catch (Exception e) {
            log.error("AI 서버 통신 중 예외 발생: {}", e.getMessage(), e);
            throw new RuntimeException("AI 서버 통신 중 오류가 발생했습니다.");
        }
    }

    @Override
    public VoiceResponseDto getAiVoiceReply(Long discussionId, Long articleId, MultipartFile audioFile, AiChatLevelEnum level, User user) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleException.ArticleNotFoundException(articleId));

        // 첫 메세지면 새 토론 생성
        Discussion discussion;
        AiChatModeEnum mode = AiChatModeEnum.FOLLOWUP;

        if (discussionId == null) {
            discussion = discussionRepository.save(
                    Discussion.builder()
                            .article(article)
                            .user(user)
                            .build()
            );

            user.addPoint(10);
            userRepository.save(user);
        } else {
            discussion = discussionRepository.findById(discussionId)
                    .orElseThrow(() -> new DiscussionException.DiscussionNotFoundException(discussionId));
        }

        String message = sttService.transcribe(audioFile, 16000);
        System.out.println("stt 변환: " + message);

        // 현재 유저 메세지 저장
        discussionContentRepository.save(
                DiscussionContent.builder()
                        .discussion(discussion)
                        .role(DiscussionRoleEnum.USER)
                        .content(message)
                        .build()
        );

        // AI 요청 DTO
        AiRequestDto aiRequest = new AiRequestDto(
                user.getId().toString(),
                discussion.getId().toString(),
                article.getDescription(),
                message,
                mode,
                level
        );

        String result = requestAiDiscussion(aiRequest);

        discussionContentRepository.save(
                DiscussionContent.builder()
                        .discussion(discussion)
                        .role(DiscussionRoleEnum.AI)
                        .content(result)
                        .build()
        );

        String outputAudio = ttsService.synthesizeToBase64(result);

        return new VoiceResponseDto(
                outputAudio,
                discussion.getId(),
                article.getTitle()
        );
    }
}
