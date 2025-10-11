package HearDay.spring.domain.wordBookmark.service.wordService;

import HearDay.spring.domain.user.entity.User;
import HearDay.spring.domain.wordBookmark.dto.response.*;
import HearDay.spring.domain.wordBookmark.entity.UserWordBookmark;
import HearDay.spring.domain.wordBookmark.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WordQueryServiceImpl implements WordQueryService {

    private final WordRepository wordRepository;

    @Override
    public WordDescriptionResponseDto getWordDescription(User user, Long wordsId) {
        return wordRepository.findWordByUserAndWordId(user, wordsId);
    }

    @Override
    public WordByDateResponseDto getWordDates(User user, LocalDate date) {
        List<UserWordBookmark> words = wordRepository.getWordByDates(user, date);

        List<WordInfoDto> wordDtos = words.stream()
                .map(w -> new WordInfoDto(w.getId(), w.getWord()))
                .toList();

        return new WordByDateResponseDto(wordDtos);
    }

    @Override
    public WordCountResponseDto getWordCounts(User user, int year, int month) {
        List<WordCountInfoResponseDto> counts = wordRepository.findWordCountsByMonth(user, year, month);
        return new WordCountResponseDto(counts);
    }
}
