package HearDay.spring.domain.user.service;

import HearDay.spring.common.enums.CategoryEnum;
import HearDay.spring.domain.user.dto.request.KakaoRequestDto;
import HearDay.spring.domain.user.dto.request.UserLoginRequestDto;
import HearDay.spring.domain.user.dto.request.UserPasswordRequestDto;
import HearDay.spring.domain.user.dto.request.UserRequestDto;
import HearDay.spring.domain.user.dto.response.UserLoginResponseDto;
import HearDay.spring.domain.user.dto.response.UserResponseDto;
import HearDay.spring.domain.user.entity.User;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface UserCommandService {
    UserLoginResponseDto registerUser(UserRequestDto request);
    void sendUserIdToEmail(String email);
    void changePassword(UserPasswordRequestDto request);
    UserLoginResponseDto loginUser(UserLoginRequestDto request);
    String loginKakaoUser(String code, HttpServletResponse httpServletResponse);
    void registerCategories(List<CategoryEnum> request, User user);
}
