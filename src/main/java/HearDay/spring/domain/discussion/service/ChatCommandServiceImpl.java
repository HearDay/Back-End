package HearDay.spring.domain.discussion.service;

import HearDay.spring.common.enums.DiscussionRoleEnum;
import HearDay.spring.domain.article.entity.Article;
import HearDay.spring.domain.article.exception.ArticleException;
import HearDay.spring.domain.article.repository.ArticleRepository;
import HearDay.spring.domain.discussion.dto.AiRequestDto;
import HearDay.spring.domain.discussion.dto.ChatResponseDto;
import HearDay.spring.domain.discussion.entity.Discussion;
import HearDay.spring.domain.discussion.entity.DiscussionContent;
import HearDay.spring.domain.discussion.exception.DiscussionException;
import HearDay.spring.domain.discussion.repository.DiscussionContentRepository;
import HearDay.spring.domain.discussion.repository.DiscussionRepository;
import HearDay.spring.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatCommandServiceImpl implements ChatCommandService {
    private final WebClient webClient;
    private final ArticleRepository articleRepository;
    private final DiscussionContentRepository discussionContentRepository;
    private final DiscussionRepository discussionRepository;

    @Override
    public Mono<ChatResponseDto> getAiReply(String request, Long articleId, Long discussionId, User user) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleException.ArticleNotFoundException(articleId));

        // 첫 메세지면 새 토론 생성
        Discussion discussion;
        List<AiRequestDto.Message> previousMessages = List.of();

        if (discussionId == null) {
            discussion = discussionRepository.save(
                    Discussion.builder()
                            .article(article)
                            .user(user)
                            .build()
            );
        } else {
            discussion = discussionRepository.findById(discussionId)
                    .orElseThrow(() -> new DiscussionException.DiscussionNotFoundException(discussionId));

            // 이전 메세지 불러오기
            previousMessages = discussionContentRepository
                    .findByDiscussionIdOrderByIdAsc(discussion.getId())
                    .stream()
                    .map(m -> new AiRequestDto.Message(m.getRole(), m.getContent()))
                    .toList();
        }

        // 현재 유저 메세지 저장
        discussionContentRepository.save(
                DiscussionContent.builder()
                        .discussion(discussion)
                        .role(DiscussionRoleEnum.USER)
                        .content(request)
                        .build()
        );

        // AI 요청 DTO
        AiRequestDto aiRequest = new AiRequestDto(
                discussion.getId(),
                user.getNickname(),
                article.getTitle(),
                article.getDescription(),
                previousMessages,
                request
        );

        // AI 서버 호출
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/chat") // AI 서버 uri 자리
                        .queryParam("discussionId", discussion.getId())
                        .build())
                .bodyValue(aiRequest)
                .retrieve()
                .bodyToMono(ChatResponseDto.class)
                .doOnNext(response -> {
                    // AI 응답 DB에 저장
                    discussionContentRepository.save(
                            DiscussionContent.builder()
                                    .discussion(discussion)
                                    .role(DiscussionRoleEnum.AI)
                                    .content(response.reply())
                                    .build()
                    );
                });
    }
}
