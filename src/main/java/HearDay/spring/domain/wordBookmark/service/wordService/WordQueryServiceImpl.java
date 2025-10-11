package HearDay.spring.domain.wordBookmark.service.wordService;

import HearDay.spring.domain.wordBookmark.dto.response.WordDescriptionResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class WordQueryServiceImpl implements WordQueryService {

    @Override
    public WordDescriptionResponseDto getWordDescription(@PathVariable String wordsId) {

    }
}
