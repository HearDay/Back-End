package HearDay.spring.domain.wordBookmark.repository;

import HearDay.spring.domain.wordBookmark.entity.UserWordBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordRepository extends JpaRepository<UserWordBookmark, Long>, WordRepositoryCustom {

}
