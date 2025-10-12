package HearDay.spring.domain.wordbookmark.repository;

import HearDay.spring.domain.user.entity.User;
import HearDay.spring.domain.wordbookmark.dto.response.WordCountInfoResponseDto;
import HearDay.spring.domain.wordbookmark.dto.response.WordDescriptionResponseDto;
import HearDay.spring.domain.wordbookmark.entity.UserWordBookmark;

import java.time.LocalDate;
import java.util.List;

public interface WordRepositoryCustom {
    WordDescriptionResponseDto findWordByUserAndWordId(User user, Long wordsId);
    boolean checkTodayWordBookmark(User user, String word);
    List<UserWordBookmark> getWordByDates(User user, LocalDate date);
    List<WordCountInfoResponseDto> findWordCountsByMonth(User user, int year, int month);
}
