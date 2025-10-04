package HearDay.spring.domain.user.service;

import HearDay.spring.domain.user.dto.request.UserRequestDto;

public interface UserCommandService {
    void registerUser(UserRequestDto request);
}
