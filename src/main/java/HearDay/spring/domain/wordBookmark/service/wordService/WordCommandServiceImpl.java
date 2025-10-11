package HearDay.spring.domain.wordBookmark.service.wordService;

import HearDay.spring.domain.user.entity.User;
import HearDay.spring.domain.wordBookmark.dto.request.WordRequestDto;
import HearDay.spring.domain.wordBookmark.entity.UserWordBookmark;
import HearDay.spring.domain.wordBookmark.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WordCommandServiceImpl implements WordCommandService {

    private final WordRepository wordRepository;

    @Override
    public void createWordBookmark(User user, WordRequestDto request) {
        UserWordBookmark word = UserWordBookmark.builder()
                .word(request.word())
                .description(request.description())
                .user(user)
                .build();

        wordRepository.save(word);
    }
}
