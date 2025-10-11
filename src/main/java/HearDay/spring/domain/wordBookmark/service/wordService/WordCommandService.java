package HearDay.spring.domain.wordBookmark.service.wordService;

import HearDay.spring.domain.user.entity.User;
import HearDay.spring.domain.wordBookmark.dto.request.WordRequestDto;

public interface WordCommandService {
    void createWordBookmark(User user, WordRequestDto wordRequestDto);
}
