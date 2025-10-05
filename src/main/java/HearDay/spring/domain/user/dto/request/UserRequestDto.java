package HearDay.spring.domain.user.dto.request;

import HearDay.spring.common.enums.CategoryEnum;
import jakarta.validation.constraints.*;
import java.util.List;

public record UserRequestDto(
        @NotBlank(message = "아이디는 필수입니다.")
        String loginId,

        @NotBlank(message = "비밀번호는 필수입니다.")
        String password,

        @Email(message = "올바른 이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "전화번호는 필수입니다.")
        @Pattern(regexp = "^\\d{10,11}$", message = "전화번호 형식이 올바르지 않습니다.")
        String phone,

        @NotEmpty(message = "선호도 입력은 필수입니다.")
        List<CategoryEnum> userCategory
) {}
