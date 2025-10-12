package HearDay.spring.domain.wordbookmark.dto.request;

import jakarta.validation.constraints.NotBlank;

public record WordRequestDto(
        @NotBlank(message = "단어는 필수입니다.")
        String word,

        @NotBlank(message = "설명은 필수입니다.")
        String description
) {
}
