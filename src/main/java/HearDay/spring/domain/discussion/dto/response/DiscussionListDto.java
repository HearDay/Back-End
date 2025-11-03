package HearDay.spring.domain.discussion.dto.response;

import java.util.List;

public record DiscussionListDto (
    List<DiscussionDto> discussionList
){
    public record DiscussionDto (
            Long discussionId,
            String articleTitle,
            String date
    ) {}
}
