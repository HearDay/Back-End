package HearDay.spring.domain.user.service;

import HearDay.spring.common.dto.code.status.ErrorStatus;
import HearDay.spring.common.dto.exception.GeneralException;
import HearDay.spring.domain.user.dto.request.UserRequestDto;
import HearDay.spring.domain.user.entity.User;
import HearDay.spring.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(UserRequestDto request) {
        if (userRepository.findByLoginId(request.getLoginId()).isPresent()) {
            throw new GeneralException(ErrorStatus.LOGIN_ID_EXIST);
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new GeneralException(ErrorStatus.EMAIL_EXIST);
        }

        User user = User.builder()
                .loginId(request.getLoginId())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .phone(request.getPhone())
                .level(1)
                .userCategory(request.getUserCategory())
                .build();

        userRepository.save(user);
    }
}
