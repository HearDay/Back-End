package HearDay.spring.domain.user.service;

import HearDay.spring.domain.user.dto.request.UserLoginRequestDto;
import HearDay.spring.domain.user.dto.request.UserPasswordRequestDto;
import HearDay.spring.domain.user.dto.request.UserRequestDto;
import HearDay.spring.domain.user.dto.response.UserLoginResponseDto;
import HearDay.spring.domain.user.dto.response.UserResponseDto;
import jakarta.servlet.http.HttpServletResponse;

public interface UserCommandService {
    UserLoginResponseDto registerUser(UserRequestDto request);
    void sendUserIdToEmail(String email);
    void changePassword(UserPasswordRequestDto request);
    UserLoginResponseDto loginUser(UserLoginRequestDto request);
    UserLoginResponseDto loginKakaoUser(String code, HttpServletResponse httpServletResponse);
}
