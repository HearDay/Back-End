package HearDay.spring.domain.user.dto.response;

public record HomeResponseDto(
        Integer level,
        String nickname,
        String updateTime
) {
}
