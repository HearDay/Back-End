package HearDay.spring.domain.discussion.service;

import HearDay.spring.domain.discussion.dto.response.DiscussionListDto;
import HearDay.spring.domain.discussion.entity.Discussion;
import HearDay.spring.domain.discussion.repository.DiscussionRepository;
import HearDay.spring.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscussionQueryServiceImpl implements DiscussionQueryService {
    private final DiscussionRepository discussionRepository;

    @Override
    public DiscussionListDto getUserDiscussions(User user, Pageable pageable) {
        Page<Discussion> discussions = discussionRepository.findByUserId(user.getId(), pageable);

        List<DiscussionListDto.DiscussionDto> discussionDtos = discussions.stream()
                .map(d -> new DiscussionListDto.DiscussionDto(
                        d.getId(),
                        d.getArticle().getTitle(),
                        d.getCreatedAt().toLocalDate().toString()
                ))
                .toList();

        return new DiscussionListDto(discussionDtos);
    }
}
