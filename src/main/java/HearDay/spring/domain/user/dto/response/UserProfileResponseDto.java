package HearDay.spring.domain.user.dto.response;

import java.time.LocalDate;
import java.util.List;

public record UserProfileResponseDto(
        String nickname,
        String email,
        Integer level,
        Integer point,
        List<Attendance> attendance
) {
    public record Attendance(
            LocalDate date
    ) {}
}
