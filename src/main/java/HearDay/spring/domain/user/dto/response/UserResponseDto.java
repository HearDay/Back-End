package HearDay.spring.domain.user.dto.response;

public record UserResponseDto (
    Long id,
    String nickname,
    String email,
    String phone,
    int level,
    String token
) {}