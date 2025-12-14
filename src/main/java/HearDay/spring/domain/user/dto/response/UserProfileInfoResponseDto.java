package HearDay.spring.domain.user.dto.response;

import HearDay.spring.common.enums.Gender;

public record UserProfileInfoResponseDto(
        String nickname,
        Gender gender,
        Integer age,
        String phone,
        String email
) {
}
