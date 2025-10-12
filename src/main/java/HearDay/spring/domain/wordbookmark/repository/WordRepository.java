package HearDay.spring.domain.wordbookmark.repository;

import HearDay.spring.domain.wordbookmark.entity.UserWordBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordRepository extends JpaRepository<UserWordBookmark, Long>, WordRepositoryCustom {

}
