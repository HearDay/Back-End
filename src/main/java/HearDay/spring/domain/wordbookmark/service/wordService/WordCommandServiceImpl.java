package HearDay.spring.domain.wordbookmark.service.wordService;

import HearDay.spring.domain.user.entity.User;
import HearDay.spring.domain.wordbookmark.dto.request.WordRequestDto;
import HearDay.spring.domain.wordbookmark.entity.UserWordBookmark;
import HearDay.spring.domain.wordbookmark.exception.WordException;
import HearDay.spring.domain.wordbookmark.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WordCommandServiceImpl implements WordCommandService {

    private final WordRepository wordRepository;

    @Override
    public void createWordBookmark(User user, WordRequestDto request) {
        if (wordRepository.checkTodayWordBookmark(user, request.word())) {
            throw new WordException.WordAlreadyBookmarkedException();
        }

        UserWordBookmark word = UserWordBookmark.builder()
                .word(request.word())
                .description(request.description())
                .user(user)
                .build();

        wordRepository.save(word);
    }
}
