package HearDay.spring.domain.user.dto.response;

import HearDay.spring.common.enums.AlarmDayType;
import HearDay.spring.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "알람 시간 응답 DTO")
public record AlarmTimeResponse(
        @Schema(description = "알람 시 (0-23)", example = "8") Integer hour,
        @Schema(description = "알람 분 (0-59)", example = "20") Integer minute,
        @Schema(description = "알람 요일 타입 (EVERYDAY/WEEKDAY/WEEKEND)", example = "WEEKDAY") AlarmDayType dayType
) {
    public static AlarmTimeResponse from(User user) {
        return new AlarmTimeResponse(
                user.getAlarmHour(),
                user.getAlarmMinute(),
                user.getAlarmDayType()
        );
    }
}
