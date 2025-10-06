package HearDay.spring.domain.user.dto.response;

public record UserResponseDto (
    Long id,
    String loginId,
    String email,
    String phone,
    int level,
    String token
) {}