package HearDay.spring.domain.wordbookmark.service.wordService;

import HearDay.spring.domain.user.entity.User;
import HearDay.spring.domain.wordbookmark.dto.response.WordByDateResponseDto;
import HearDay.spring.domain.wordbookmark.dto.response.WordCountResponseDto;
import HearDay.spring.domain.wordbookmark.dto.response.WordDescriptionResponseDto;

import java.time.LocalDate;

public interface WordQueryService {
    WordDescriptionResponseDto getWordDescription(User user, Long wordsId);
    WordByDateResponseDto getWordDates(User user, LocalDate date);
    WordCountResponseDto getWordCounts(User user, int year, int month);
}
