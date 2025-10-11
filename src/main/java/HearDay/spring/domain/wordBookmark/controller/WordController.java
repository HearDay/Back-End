package HearDay.spring.domain.wordBookmark.controller;

import HearDay.spring.common.dto.response.CommonApiResponse;
import HearDay.spring.domain.user.entity.User;
import HearDay.spring.domain.wordBookmark.dto.request.WordRequestDto;
import HearDay.spring.domain.wordBookmark.dto.response.WordByDateResponseDto;
import HearDay.spring.domain.wordBookmark.dto.response.WordDescriptionResponseDto;
import HearDay.spring.domain.wordBookmark.service.wordService.WordCommandService;
import HearDay.spring.domain.wordBookmark.service.wordService.WordQueryService;
import HearDay.spring.global.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/words")
@RequiredArgsConstructor
public class WordController {

    private final WordCommandService wordCommandService;
    private final WordQueryService wordQueryService;

    @PostMapping("/")
    @Operation(summary = "단어 저장 API", description = "단어를 내 단어장에 저장시 사용하는 API입니다.")
    public ResponseEntity<CommonApiResponse<Void>> saveWord(
            @AuthUser User user,
            @RequestBody WordRequestDto request
    ) {
        wordCommandService.createWordBookmark(user, request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonApiResponse.success("단어 저장에 성공했습니다.", null));
    }

    @GetMapping("/{wordsId}")
    @Operation(summary = "단어 뜻 조회 API", description = "저장된 단어 뜻 확인시 사용하는 API입니다.")
    public ResponseEntity<CommonApiResponse<WordDescriptionResponseDto>> getWord(
            @AuthUser User user,
            @PathVariable Long wordsId
    ) {
        WordDescriptionResponseDto result = wordQueryService.getWordDescription(user, wordsId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonApiResponse.success("단어 뜻 조회에 성공했습니다.", result));
    }

    @GetMapping("/date")
    @Operation(summary = "특정 날짜의 단어 목록 조회 API", description = "원하는 날짜를 쿼리 파라미터로 보내면 그날 저장된 단어를 출력하는 API입니다.")
    public ResponseEntity<CommonApiResponse<List<WordByDateResponseDto>>> getWordDates(
            @AuthUser User user,
            @RequestParam LocalDate date
    ) {
        List<WordByDateResponseDto> result = wordQueryService.getWordDates(user, date);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonApiResponse.success("해당 날짜의 단어 조회에 성공했습니다.", result));
    }
}
