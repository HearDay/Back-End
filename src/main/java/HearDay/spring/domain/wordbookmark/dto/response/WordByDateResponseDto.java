package HearDay.spring.domain.wordbookmark.dto.response;

import java.util.List;

public record WordByDateResponseDto(
    List<WordInfoDto> words
) {
}
