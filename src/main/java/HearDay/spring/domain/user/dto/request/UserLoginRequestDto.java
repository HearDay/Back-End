package HearDay.spring.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequestDto(
        @NotBlank(message = "아이디는 필수입니다.")
        String LoginId,

        @NotBlank(message = "비밀번호는 필수입니다.")
        String password
) {
}
