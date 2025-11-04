package HearDay.spring.domain.discussion.repository;

import HearDay.spring.domain.discussion.entity.DiscussionContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscussionContentRepository extends JpaRepository<DiscussionContent, Long> {
    List<DiscussionContent> findByDiscussionIdOrderByIdAsc(Long discussionId);
    List<DiscussionContent> findByDiscussionIdOrderByCreatedAtDesc(Long discussionId);
}
