package HearDay.spring.domain.user.controller;

import HearDay.spring.common.dto.response.CommonApiResponse;
import HearDay.spring.domain.user.dto.request.UserEmailRequestDto;
import HearDay.spring.domain.user.dto.request.UserLoginRequestDto;
import HearDay.spring.domain.user.dto.request.UserPasswordRequestDto;
import HearDay.spring.domain.user.dto.request.UserRequestDto;
import HearDay.spring.domain.user.dto.response.UserLoginResponseDto;
import HearDay.spring.domain.user.dto.response.UserResponseDto;
import HearDay.spring.domain.user.service.UserCommandService;
import HearDay.spring.domain.user.service.UserQueryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

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
}
