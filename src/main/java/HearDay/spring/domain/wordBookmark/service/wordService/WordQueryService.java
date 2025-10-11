package HearDay.spring.domain.wordBookmark.service.wordService;

import HearDay.spring.domain.user.entity.User;
import HearDay.spring.domain.wordBookmark.dto.response.WordDescriptionResponseDto;

public interface WordQueryService {
    WordDescriptionResponseDto getWordDescription(User user, Long wordsId);
}
