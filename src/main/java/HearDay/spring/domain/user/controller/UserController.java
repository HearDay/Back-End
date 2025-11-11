package HearDay.spring.domain.user.controller;

import HearDay.spring.common.dto.response.CommonApiResponse;
import HearDay.spring.common.enums.CategoryEnum;
import HearDay.spring.domain.user.dto.request.*;
import HearDay.spring.domain.user.dto.response.HomeResponseDto;
import HearDay.spring.domain.user.dto.response.UserLoginResponseDto;
import HearDay.spring.domain.user.dto.response.UserResponseDto;
import HearDay.spring.domain.user.entity.User;
import HearDay.spring.domain.user.service.MailService;
import HearDay.spring.domain.user.service.RefreshTokenService;
import HearDay.spring.domain.user.service.UserCommandService;
import HearDay.spring.domain.user.service.UserQueryService;
import HearDay.spring.global.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;
    private final RefreshTokenService refreshTokenService;
    private final MailService mailService;

    @PostMapping
    @Operation(summary = "회원가입 API", description = "회원가입시 사용하는 API입니다.")
    public ResponseEntity<CommonApiResponse<UserLoginResponseDto>> signUp(
            @RequestBody UserRequestDto request
    ) {
        UserLoginResponseDto result = userCommandService.registerUser(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonApiResponse.success("회원가입에 성공했습니다.", result));
    }

    @GetMapping("/check-id")
    @Operation(summary = "아이디 중복 확인 API", description = "회원가입시 아이디 중복 확인에 사용하는 API입니다.")
    public ResponseEntity<CommonApiResponse<UserResponseDto>> checkUserId(
            @RequestParam String userLoginId
    ) {
        userQueryService.checkId(userLoginId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonApiResponse.success("중복된 아이디가 없습니다.", null));
    }

    @PostMapping("/find-id")
    @Operation(summary = "아이디 찾기 API", description = "아이디 찾기에 사용하는 API입니다.")
    public ResponseEntity<CommonApiResponse<Void>> findUserId(
            @RequestBody UserEmailRequestDto request
    ) {
        userCommandService.sendUserIdToEmail(request.email());
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonApiResponse.success("아이디를 이메일로 전송했습니다.", null));
    }

    @PostMapping("/password/reset")
    @Operation(summary = "새 비밀번호 설정 API", description = "이메일 인증 후 새 비밀번호를 설정하는 API입니다.")
    public ResponseEntity<CommonApiResponse<Void>> resetPassword(
            @RequestBody UserPasswordRequestDto request
    ) {
        userCommandService.changePassword(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonApiResponse.success("비밀번호 변경에 성공했습니다.", null));
    }

    @PostMapping("/login")
    @Operation(summary = "일반 로그인 API", description = "일반 로그인 API입니다.")
    public ResponseEntity<CommonApiResponse<UserLoginResponseDto>> login(
            @RequestBody UserLoginRequestDto request
    ) {
        UserLoginResponseDto result = userCommandService.loginUser(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonApiResponse.success("로그인에 성공했습니다.", result));
    }

    @Value("${backend.url")
    private String backendUrl;

    @GetMapping("/login/kakao")
    @Operation(summary = "카카오 로그인 API", description = "카카오 로그인 API입니다.")
    public void loginKakao(
            @RequestParam String code, HttpServletResponse httpServletResponse
    ) throws IOException {
        UserLoginResponseDto result = userCommandService.loginKakaoUser(code, httpServletResponse);
        String redirectUrl = backendUrl + "/login/success?accessToken=" + result.accessToken() + "&refreshToken=" + result.refreshToken();

        httpServletResponse.sendRedirect(redirectUrl);
    }

    @PostMapping("/category")
    @Operation(summary = "사용자 선호 카테고리 등록 API", description = "회원가입 시 사용하는 카테고리 등록 API입니다.")
    public ResponseEntity<CommonApiResponse<Void>> registerCategory(
            @RequestBody List<CategoryEnum> request,
            @AuthUser User user
    ) {
        userCommandService.registerCategories(request, user);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonApiResponse.success("카테고리 등록에 성공했습니다.", null));
    }

    @GetMapping("/home")
    @Operation(summary = "홈화면에서 유저 정보 조회 API", description = "홈화면에서 유저 정보를 불러오는 API입니다.")
    public ResponseEntity<CommonApiResponse<HomeResponseDto>> getInformation(
            @AuthUser User user
    ) {
        HomeResponseDto result = userQueryService.getHomeInformation(user);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonApiResponse.success(result));
    }

    @PostMapping("/refresh")
    @Operation(summary = "리프래시 토큰 재발급 API")
    public ResponseEntity<CommonApiResponse<?>> reissueToken(
            @RequestBody String refreshToken
    ) {
        String result = userCommandService.refreshAccessToken(refreshToken);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonApiResponse.success(result));
    }

    @PostMapping("/send")
    public ResponseEntity<CommonApiResponse<Void>> sendEmail(
            @RequestBody String email
    ) {
        userCommandService.sendAuthCode(email);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonApiResponse.success(null));
    }

    @PostMapping("/verify")
    public ResponseEntity<CommonApiResponse<Void>> verifyEmail(
            @RequestBody String email,
            @RequestParam String code
    ) {
        boolean result = mailService.verifyCode(email, code);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonApiResponse.success(null));
    }
}
