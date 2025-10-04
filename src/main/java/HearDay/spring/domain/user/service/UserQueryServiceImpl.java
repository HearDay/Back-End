package HearDay.spring.domain.user.service;

import HearDay.spring.common.dto.code.status.ErrorStatus;
import HearDay.spring.common.dto.exception.GeneralException;
import HearDay.spring.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;

    @Override
    public void checkId(String userLoginId) {
        if (userRepository.findByLoginId(userLoginId).isPresent()) {
            throw new GeneralException(ErrorStatus.LOGIN_ID_EXIST);
        }
    }
}
