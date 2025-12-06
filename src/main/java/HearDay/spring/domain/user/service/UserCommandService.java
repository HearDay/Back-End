package HearDay.spring.domain.user.service;

import HearDay.spring.common.enums.AlarmDayType;
import HearDay.spring.domain.user.dto.request.*;
import HearDay.spring.domain.user.dto.response.KakaoLoginResponseDto;
import HearDay.spring.domain.user.dto.response.UserGenderAgeResponseDto;
import HearDay.spring.domain.user.dto.response.UserLoginResponseDto;
import HearDay.spring.domain.user.entity.User;
import jakarta.servlet.http.HttpServletResponse;

public interface UserCommandService {
    UserLoginResponseDto registerUser(UserRequestDto request);
    void sendUserIdToEmail(String email);
    void changePassword(UserPasswordRequestDto request);
    UserLoginResponseDto loginUser(UserLoginRequestDto request);
    KakaoLoginResponseDto loginKakaoUser(String code, HttpServletResponse httpServletResponse);
    void registerCategories(UserInfoRequestDto request, User user);
    String refreshAccessToken(String refreshToken);
    void sendAuthCode(String email);
    UserGenderAgeResponseDto updateGenderAndAge(User user, UserGenderAgeUpdateRequestDto request);
    void updateAlarmTime(User user, Integer hour, Integer minute, AlarmDayType dayType);
}
