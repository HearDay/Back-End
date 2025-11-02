package HearDay.spring.domain.discussion.repository;

import HearDay.spring.domain.discussion.entity.Discussion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscussionRepository extends JpaRepository<Discussion, Long> {
}
