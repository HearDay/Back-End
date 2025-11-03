package HearDay.spring.domain.discussion.controller;

import HearDay.spring.common.dto.response.CommonApiResponse;
import HearDay.spring.domain.discussion.dto.response.DiscussionListDto;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/discussion")
public class DiscussionController {

    private final DiscussionQueryService discussionQueryService;

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
}
