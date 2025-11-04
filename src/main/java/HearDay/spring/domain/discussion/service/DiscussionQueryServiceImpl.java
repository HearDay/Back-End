package HearDay.spring.domain.discussion.service;

import HearDay.spring.domain.discussion.dto.response.DiscussionContentDto;
import HearDay.spring.domain.discussion.dto.response.DiscussionListDto;
import HearDay.spring.domain.discussion.entity.Discussion;
import HearDay.spring.domain.discussion.entity.DiscussionContent;
import HearDay.spring.domain.discussion.repository.DiscussionContentRepository;
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
    private final DiscussionContentRepository discussionContentRepository;

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

    @Override
    public DiscussionContentDto getDiscussionContent(User user, Long discussionId) {
        List<DiscussionContent> contents = discussionContentRepository.findByDiscussionIdOrderByCreatedAtDesc(discussionId);

        List<DiscussionContentDto.Content> contentDtos = contents.stream()
                .map(c -> new DiscussionContentDto.Content(
                        c.getId(),
                        c.getRole(),
                        c.getContent()
                ))
                .toList();

        return new DiscussionContentDto(contentDtos);
    }
}
