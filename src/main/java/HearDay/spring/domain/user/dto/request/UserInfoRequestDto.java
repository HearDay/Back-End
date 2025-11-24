package HearDay.spring.domain.user.dto.request;

import HearDay.spring.common.enums.AlarmDayType;
import HearDay.spring.common.enums.CategoryEnum;
import HearDay.spring.common.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.List;

public record UserInfoRequestDto(
        List<CategoryEnum> category,

        @Schema(description = "성별",  example = "M")
        Gender gender,

        @Min(value = 1, message = "나이는 1세 이상이어야 합니다.")
        @Max(value = 150, message = "나이는 150세 이하여야 합니다.")
        Integer age,

        Integer hour,
        Integer minute,
        AlarmDayType dayType
) {
}
