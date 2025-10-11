package HearDay.spring.domain.wordBookmark.service.wordService;

import HearDay.spring.domain.user.entity.User;
import HearDay.spring.domain.wordBookmark.dto.response.WordDescriptionResponseDto;
import HearDay.spring.domain.wordBookmark.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WordQueryServiceImpl implements WordQueryService {

    private final WordRepository wordRepository;

    @Override
    public WordDescriptionResponseDto getWordDescription(User user, Long wordsId) {
        return wordRepository.findWordByUserAndWordId(user, wordsId);
    }
}
