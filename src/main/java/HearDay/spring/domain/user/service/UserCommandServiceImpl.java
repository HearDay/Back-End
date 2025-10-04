package HearDay.spring.domain.user.service;

import HearDay.spring.domain.user.dto.request.UserRequestDto;
import HearDay.spring.domain.user.entity.User;
import HearDay.spring.domain.user.exception.UserException;
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
            throw new UserException.UserLoginIdAlreadyExistException(request.getLoginId());
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserException.UserEmailAlreadyExistException(request.getEmail());
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
