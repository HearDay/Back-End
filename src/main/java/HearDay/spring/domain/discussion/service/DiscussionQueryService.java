package HearDay.spring.domain.discussion.service;

import HearDay.spring.domain.discussion.dto.response.DiscussionListDto;
import HearDay.spring.domain.user.entity.User;
import org.springframework.data.domain.Pageable;

public interface DiscussionQueryService {
    DiscussionListDto getUserDiscussions(User user, Pageable pageable);
}
