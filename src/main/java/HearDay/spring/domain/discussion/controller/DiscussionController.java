package HearDay.spring.domain.discussion.controller;

import HearDay.spring.common.dto.response.CommonApiResponse;
import HearDay.spring.domain.discussion.dto.request.ChatRequestDto;
import HearDay.spring.domain.discussion.dto.response.ChatResponseDto;
import HearDay.spring.domain.discussion.dto.response.DiscussionContentDto;
import HearDay.spring.domain.discussion.dto.response.DiscussionListDto;
import HearDay.spring.domain.discussion.service.ChatCommandService;
import HearDay.spring.domain.discussion.service.DiscussionQueryService;
import HearDay.spring.domain.user.entity.User;
import HearDay.spring.global.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/discussion")
public class DiscussionController {

    private final DiscussionQueryService discussionQueryService;
    private final ChatCommandService chatCommandService;

    @GetMapping
    @Operation(summary = "토론 기록 전체 조회 API")
    public ResponseEntity<CommonApiResponse<DiscussionListDto>> getDiscussion(
            @AuthUser User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Sort.Direction direction = sort.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdAt"));

        DiscussionListDto result = discussionQueryService.getUserDiscussions(user, pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonApiResponse.success("조회에 성공했습니다.", result));
    }

    @GetMapping("/{discussionId}")
    @Operation(summary = "토론 기록 상세 조회 API")
    public ResponseEntity<CommonApiResponse<DiscussionContentDto>> getDiscussionContent(
            @AuthUser User user,
            @RequestParam Long discussionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Sort.Direction direction = sort.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdAt"));

        DiscussionContentDto result = discussionQueryService.getDiscussionContent(user, discussionId, pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonApiResponse.success("조회에 성공했습니다.", result));
    }

    @PostMapping("/chat/{articleId}")
    @Operation(summary = "AI 토론(채팅) API", description = "채팅 토론에 사용하는 API입니다. 채팅 첫 전송 시 discussionId는 보내지 않아도 됩니다.")
    public ResponseEntity<CommonApiResponse<ChatResponseDto>> postChat(
            @RequestParam(required = false) Long discussionId,
            @PathVariable Long articleId,
            @RequestBody ChatRequestDto request,
            @AuthUser User user
    ) {
        ChatResponseDto result = chatCommandService.getAiReply(request, articleId, discussionId, user);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonApiResponse.success("채팅 요청에 성공했습니다.", result));
    }
}
