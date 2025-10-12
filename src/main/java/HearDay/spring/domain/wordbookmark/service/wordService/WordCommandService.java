package HearDay.spring.domain.wordbookmark.service.wordService;

import HearDay.spring.domain.user.entity.User;
import HearDay.spring.domain.wordbookmark.dto.request.WordRequestDto;

public interface WordCommandService {
    void createWordBookmark(User user, WordRequestDto wordRequestDto);
}
