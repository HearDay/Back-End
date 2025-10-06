package HearDay.spring.domain.user.service;

import HearDay.spring.domain.user.dto.request.UserRequestDto;
import HearDay.spring.domain.user.dto.response.UserResponseDto;
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
//    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public UserResponseDto registerUser(UserRequestDto request) {
        if (userRepository.findByLoginId(request.loginId()).isPresent()) {
            throw new UserException.UserLoginIdAlreadyExistException(request.loginId());
        }
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new UserException.UserEmailAlreadyExistException(request.email());
        }

        User user = User.builder()
                .loginId(request.loginId())
                .password(passwordEncoder.encode(request.password()))
                .email(request.email())
                .phone(request.phone())
                .level(1)
                .userCategory(request.userCategory())
                .build();

        userRepository.save(user);

//        String token = jwtTokenProvider.generateToken(user.getEmail());
        String token = null;

        return new UserResponseDto(
                user.getId(),
                user.getLoginId(),
                user.getEmail(),
                user.getPhone(),
                user.getLevel(),
                token
        );
    }
}
