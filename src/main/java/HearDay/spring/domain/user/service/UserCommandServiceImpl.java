package HearDay.spring.domain.user.service;

import HearDay.spring.domain.user.dto.request.KakaoRequestDto;
import HearDay.spring.domain.user.dto.request.UserLoginRequestDto;
import HearDay.spring.domain.user.dto.request.UserPasswordRequestDto;
import HearDay.spring.domain.user.dto.request.UserRequestDto;
import HearDay.spring.domain.user.dto.response.KakaoResponseDto;
import HearDay.spring.domain.user.dto.response.UserLoginResponseDto;
import HearDay.spring.domain.user.dto.response.UserResponseDto;
import HearDay.spring.domain.user.entity.User;
import HearDay.spring.domain.user.exception.UserException;
import HearDay.spring.domain.user.repository.UserRepository;
import HearDay.spring.global.jwt.JwtTokenProvider;
import HearDay.spring.global.util.KakaoUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final KakaoUtil kakaoUtil;

    @Override
    public UserLoginResponseDto registerUser(UserRequestDto request) {
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

        String token = jwtTokenProvider.generateToken(request.loginId());

        return new UserLoginResponseDto(
            token
        );
    }

    @Override
    public void sendUserIdToEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException.UserNotFoundException(email));

        mailService.sendMail(
                email,
                "[HEARDAY] 아이디 찾기 안내",
                "회원님의 아이디는: " + user.getLoginId() + " 입니다."
        );
    }

    @Override
    public void changePassword(UserPasswordRequestDto request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UserException.UserNotFoundException(request.email()));

        if (passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new UserException.UserPasswordSameAsOldException(request.password());
        }

        user.changePassword(request.password(), passwordEncoder);
        userRepository.save(user);
    }

    @Override
    public UserLoginResponseDto loginUser(UserLoginRequestDto request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.LoginId(), request.password());

        authenticationManager.authenticate(authenticationToken);
        String token = jwtTokenProvider.generateToken(request.LoginId());

        return new UserLoginResponseDto(
                token
        );
    }

    @Override
    public KakaoRequestDto loginKakaoUser(String code, HttpServletResponse httpServletResponse) {
        KakaoResponseDto token = kakaoUtil.requestToken(code);
        return kakaoUtil.requestProfile(token);
    }
}
