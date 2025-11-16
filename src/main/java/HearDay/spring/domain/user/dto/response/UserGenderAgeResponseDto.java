package HearDay.spring.domain.user.dto.response;

import HearDay.spring.common.enums.Gender;
import HearDay.spring.domain.user.entity.User;

public record UserGenderAgeResponseDto(
        Long userId,
        String nickname,
        Gender gender,
        Integer age
) {
    public static UserGenderAgeResponseDto from(User user) {
        return new UserGenderAgeResponseDto(
                user.getId(),
                user.getNickname(),
                user.getGender(),
                user.getAge()
        );
    }
}
