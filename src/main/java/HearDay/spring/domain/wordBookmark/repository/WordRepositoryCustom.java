package HearDay.spring.domain.wordBookmark.repository;

import HearDay.spring.domain.user.entity.User;
import HearDay.spring.domain.wordBookmark.dto.response.WordDescriptionResponseDto;

public interface WordRepositoryCustom {
    WordDescriptionResponseDto findWordByUserAndWordId(User user, Long wordsId);
    boolean checkTodayWordBookmark(User user, String word);
}
