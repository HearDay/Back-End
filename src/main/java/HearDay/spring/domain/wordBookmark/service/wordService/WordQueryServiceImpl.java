package HearDay.spring.domain.wordBookmark.service.wordService;

import HearDay.spring.domain.user.entity.User;
import HearDay.spring.domain.wordBookmark.dto.response.WordByDateResponseDto;
import HearDay.spring.domain.wordBookmark.dto.response.WordDescriptionResponseDto;
import HearDay.spring.domain.wordBookmark.dto.response.WordInfoDto;
import HearDay.spring.domain.wordBookmark.entity.UserWordBookmark;
import HearDay.spring.domain.wordBookmark.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WordQueryServiceImpl implements WordQueryService {

    private final WordRepository wordRepository;

    @Override
    public WordDescriptionResponseDto getWordDescription(User user, Long wordsId) {
        return wordRepository.findWordByUserAndWordId(user, wordsId);
    }

    @Override
    public List<WordByDateResponseDto> getWordDates(User user, LocalDate date) {
        List<UserWordBookmark> words = wordRepository.getWordByDates(user, date);

        List<WordInfoDto> wordDtos = words.stream()
                .map(w -> new WordInfoDto(w.getId(), w.getWord()))
                .toList();

        return List.of(new WordByDateResponseDto(wordDtos));
    }
}
