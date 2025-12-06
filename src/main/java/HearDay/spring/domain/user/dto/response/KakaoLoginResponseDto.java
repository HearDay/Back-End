package HearDay.spring.domain.user.dto.response;

public record KakaoLoginResponseDto(
        String accessToken,
        String refreshToken,
        boolean isNewUser
) {
}
