package HearDay.spring.domain.wordBookmark.service.wordService;

import HearDay.spring.domain.user.entity.User;
import HearDay.spring.domain.wordBookmark.dto.response.WordByDateResponseDto;
import HearDay.spring.domain.wordBookmark.dto.response.WordDescriptionResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface WordQueryService {
    WordDescriptionResponseDto getWordDescription(User user, Long wordsId);
    List<WordByDateResponseDto> getWordDates(User user, LocalDate date);
}
