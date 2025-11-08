package HearDay.spring.domain.user.service;

import HearDay.spring.common.enums.CategoryEnum;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final KakaoUtil kakaoUtil;
    private final RefreshTokenService refreshTokenService;

    @Override
    public UserLoginResponseDto registerUser(UserRequestDto request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new UserException.UserEmailAlreadyExistException(request.email());
        }

        User user = User.builder()
                .nickname(request.nickname())
                .password(passwordEncoder.encode(request.password()))
                .email(request.email())
                .phone(request.phone())
                .level(1)
                .build();

        userRepository.save(user);

        String accessToken = jwtTokenProvider.generateToken(request.email());
        String refreshToken = jwtTokenProvider.createRefreshToken(request.email());

        refreshTokenService.saveRefreshToken(request.email(), refreshToken);

        return new UserLoginResponseDto(
                accessToken,
                refreshToken
        );
    }

    @Override
    public void sendUserIdToEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException.UserNotFoundException(email));

//        mailService.sendMail(
//                email,
//                "[HEARDAY] 아이디 찾기 안내",
//                "회원님의 아이디는: " + user.getLoginId() + " 입니다."
//        );
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
                new UsernamePasswordAuthenticationToken(request.email(), request.password());

        authenticationManager.authenticate(authenticationToken);
        String accessToken = jwtTokenProvider.generateToken(request.email());
        String refreshToken = jwtTokenProvider.createRefreshToken(request.email());

        refreshTokenService.saveRefreshToken(request.email(), refreshToken);

        return new UserLoginResponseDto(
                accessToken,
                refreshToken
        );
    }

    @Override
    public UserLoginResponseDto loginKakaoUser(String code, HttpServletResponse httpServletResponse) {
        KakaoResponseDto oAuthtoken = kakaoUtil.requestToken(code);
        KakaoRequestDto kakaoProfile = kakaoUtil.requestProfile(oAuthtoken);
        String email = kakaoProfile.kakao_account().email();

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> createNewUser(kakaoProfile));

        String accessToken = jwtTokenProvider.generateToken(user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

        refreshTokenService.saveRefreshToken(user.getEmail(), refreshToken);

        return new UserLoginResponseDto(
                accessToken,
                refreshToken
        );
    }

    private User createNewUser(KakaoRequestDto kakaoProfile) {
        User user = User.builder()
                .nickname(kakaoProfile.kakao_account().profile().nickname())
                .password(null)
                .email(kakaoProfile.kakao_account().email())
                .phone(null)
                .level(1)
                .userCategory(null)
                .build();
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void registerCategories(List<CategoryEnum> request, User user) {
        User persistedUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new UserException.UserNotFoundException(user.getEmail()));

        persistedUser.getUserCategory().addAll(request);
    }

    @Override
    public String refreshAccessToken(String refreshToken) {
        // 서명 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new UserException.RefreshTokenException();
        }

        // 만료 여부 확인
        if (jwtTokenProvider.isExpired(refreshToken)) {
            throw new UserException.RefreshTokenException();
        }

        String userEmail = jwtTokenProvider.getUsernameFromToken(refreshToken);
        String savedRefreshToken = refreshTokenService.getRefreshToken(userEmail);

        if (savedRefreshToken == null || !savedRefreshToken.equals(refreshToken)) {
            throw new UserException.RefreshTokenException();
        }

        return jwtTokenProvider.generateToken(userEmail);
    }
}
