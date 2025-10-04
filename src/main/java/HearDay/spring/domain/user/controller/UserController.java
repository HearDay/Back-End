package HearDay.spring.domain.user.controller;

import HearDay.spring.common.dto.response.CommonApiResponse;
import HearDay.spring.domain.user.dto.request.UserRequestDto;
import HearDay.spring.domain.user.service.UserCommandService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserCommandService userCommandService;

    @PostMapping("/")
    @Operation(summary = "회원가입 API", description = "회원가입시 사용하는 API입니다.")
    public ResponseEntity<CommonApiResponse<Void>> signUp(
            @RequestBody UserRequestDto request
    ) {
        userCommandService.registerUser(request);
        return ResponseEntity.ok(CommonApiResponse.success("회원가입 성공", null));
    }
}
