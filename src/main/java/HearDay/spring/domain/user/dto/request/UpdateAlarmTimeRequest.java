package HearDay.spring.domain.user.dto.request;

import HearDay.spring.common.enums.AlarmDayType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "알람 시간 설정 요청 DTO")
public record UpdateAlarmTimeRequest(
        @Schema(description = "알람 시 (0-23)", example = "8", required = true)
        @NotNull(message = "알람 시는 필수입니다.")
        @Min(value = 0, message = "시는 0 이상이어야 합니다.")
        @Max(value = 23, message = "시는 23 이하여야 합니다.")
        Integer hour,

        @Schema(description = "알람 분 (0-59)", example = "20", required = true)
        @NotNull(message = "알람 분은 필수입니다.")
        @Min(value = 0, message = "분은 0 이상이어야 합니다.")
        @Max(value = 59, message = "분은 59 이하여야 합니다.")
        Integer minute,

        @Schema(description = "알람 요일 타입 (EVERYDAY/WEEKDAY/WEEKEND)", example = "WEEKDAY", required = true)
        @NotNull(message = "알람 요일 타입은 필수입니다.")
        AlarmDayType dayType
) {
}
