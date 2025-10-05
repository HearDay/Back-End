package HearDay.spring.domain.user.controller;

import HearDay.spring.common.dto.response.CommonApiResponse;
import HearDay.spring.domain.user.dto.request.UserRequestDto;
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

    @PostMapping("/")
    @Operation(summary = "회원가입 API", description = "회원가입시 사용하는 API입니다.")
    public ResponseEntity<CommonApiResponse<Void>> signUp(
            @RequestBody UserRequestDto request
    ) {
        userCommandService.registerUser(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonApiResponse.success("회원가입에 성공했습니다.", null));
    }

    @GetMapping("/check-id")
    @Operation(summary = "아이디 중복 확인 API", description = "회원가입시 아이디 중복 확인에 사용하는 API입니다.")
    public ResponseEntity<CommonApiResponse<UserResponseDto>> checkUserId(
            @RequestParam String userLoginId
    ) {
//        UserResponseDto responseDto = userQueryService.checkId(userLoginId);
        return ResponseEntity.ok(CommonApiResponse.success("중복된 아이디가 없습니다.", null));
    }
}
