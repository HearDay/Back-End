package HearDay.spring.domain.wordbookmark.dto.response;

import java.time.LocalDate;

public record WordCountInfoResponseDto(
        LocalDate date,
        long count
) {
}
