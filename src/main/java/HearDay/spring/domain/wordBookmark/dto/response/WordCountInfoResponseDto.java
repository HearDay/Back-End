package HearDay.spring.domain.wordBookmark.dto.response;

import java.time.LocalDate;

public record WordCountInfoResponseDto(
        LocalDate date,
        long count
) {
}
