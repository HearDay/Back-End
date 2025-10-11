package HearDay.spring.domain.wordBookmark.service.wordService;

import HearDay.spring.domain.user.entity.User;
import HearDay.spring.domain.wordBookmark.dto.response.WordByDateResponseDto;
import HearDay.spring.domain.wordBookmark.dto.response.WordCountResponseDto;
import HearDay.spring.domain.wordBookmark.dto.response.WordDescriptionResponseDto;

import java.time.LocalDate;

public interface WordQueryService {
    WordDescriptionResponseDto getWordDescription(User user, Long wordsId);
    WordByDateResponseDto getWordDates(User user, LocalDate date);
    WordCountResponseDto getWordCounts(User user, int year, int month);
}
