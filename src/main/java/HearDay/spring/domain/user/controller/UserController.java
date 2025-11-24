package HearDay.spring.domain.user.controller;

import HearDay.spring.common.dto.response.CommonApiResponse;
import HearDay.spring.common.enums.CategoryEnum;
import HearDay.spring.domain.user.dto.request.*;
import HearDay.spring.domain.user.dto.response.AlarmTimeResponse;
import HearDay.spring.domain.user.dto.response.HomeResponseDto;
import HearDay.spring.domain.user.dto.response.UserGenderAgeResponseDto;
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
import jakarta.validation.Valid;
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
    @Operation(summary = "사용자 정보(카테고리, 성별, 나이, 알림) 등록 API", description = "회원가입 시 사용하는 카테고리 등록 API입니다.")
    public ResponseEntity<CommonApiResponse<Void>> registerCategory(
            @RequestBody UserInfoRequestDto request,
            @AuthUser User user
    ) {
        userCommandService.registerCategories(request, user);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonApiResponse.success("user 정보 등록에 성공했습니다.", null));
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
    @Operation(summary = "이메일 인증 요청 API")
    public ResponseEntity<CommonApiResponse<Void>> sendEmail(
            @RequestBody String email
    ) {
        userCommandService.sendAuthCode(email);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonApiResponse.success(null));
    }

    @PostMapping("/verify")
    @Operation(summary = "이메일 검증 API")
    public ResponseEntity<CommonApiResponse<Void>> verifyEmail(
            @RequestBody String email,
            @RequestParam String code
    ) {
        boolean result = mailService.verifyCode(email, code);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonApiResponse.success(null));
    }

    @GetMapping("/profile/gender-age")
    @Operation(summary = "성별 및 나이 조회 API", description = "사용자의 성별과 나이 정보를 조회하는 API입니다.")
    public ResponseEntity<CommonApiResponse<UserGenderAgeResponseDto>> getGenderAndAge(
            @AuthUser User user
    ) {
        UserGenderAgeResponseDto result = userQueryService.getGenderAndAge(user);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonApiResponse.success("성별 및 나이 조회에 성공했습니다.", result));
    }

    @PatchMapping("/profile/gender-age")
    @Operation(summary = "성별 및 나이 수정 API", description = "사용자의 성별과 나이 정보를 수정하는 API입니다. {M/F/UNKNOWN}")
    public ResponseEntity<CommonApiResponse<UserGenderAgeResponseDto>> updateGenderAndAge(
            @AuthUser User user,
            @Valid @RequestBody UserGenderAgeUpdateRequestDto request
    ) {
        UserGenderAgeResponseDto result = userCommandService.updateGenderAndAge(user, request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonApiResponse.success("성별 및 나이 수정에 성공했습니다.", result));
    }

    @GetMapping("/alarm")
    @Operation(summary = "알람 시간 조회 API", description = "사용자의 알람 시간 설정을 조회하는 API입니다.")
    public ResponseEntity<CommonApiResponse<AlarmTimeResponse>> getAlarmTime(
            @AuthUser User user
    ) {
        AlarmTimeResponse result = AlarmTimeResponse.from(user);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonApiResponse.success("알람 시간 조회에 성공했습니다.", result));
    }

    @PatchMapping("/alarm")
    @Operation(summary = "알람 시간 설정 API", description = "사용자의 알람 시간을 설정하는 API입니다.")
    public ResponseEntity<CommonApiResponse<Void>> updateAlarmTime(
            @AuthUser User user,
            @Valid @RequestBody UpdateAlarmTimeRequest request
    ) {
        userCommandService.updateAlarmTime(user, request.hour(), request.minute(), request.dayType());
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonApiResponse.success("알람 시간 설정에 성공했습니다.", null));
    }
}
