package HearDay.spring.domain.wordBookmark.dto.response;

import java.util.List;

public record WordCountResponseDto(
        List<WordCountInfoResponseDto> wordCountList
) {
}
