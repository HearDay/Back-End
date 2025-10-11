package HearDay.spring.domain.wordBookmark.service.wordService;

import HearDay.spring.domain.wordBookmark.dto.response.WordDescriptionResponseDto;
import org.springframework.web.bind.annotation.PathVariable;

public interface WordQueryService {
    WordDescriptionResponseDto getWordDescription(@PathVariable String wordsId);
}
