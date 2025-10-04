package HearDay.spring.domain.user.service;

import HearDay.spring.domain.user.dto.request.UserRequestDto;
import HearDay.spring.domain.user.entity.User;

public interface UserCommandService {
    void registerUser(UserRequestDto request);
}
