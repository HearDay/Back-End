package HearDay.spring.domain.user.service;

import HearDay.spring.domain.user.dto.request.UserPasswordRequestDto;
import HearDay.spring.domain.user.dto.request.UserRequestDto;
import HearDay.spring.domain.user.dto.response.UserResponseDto;

public interface UserCommandService {
    UserResponseDto registerUser(UserRequestDto request);
    void sendUserIdToEmail(String email);
    void changePassword(UserPasswordRequestDto request);
}
