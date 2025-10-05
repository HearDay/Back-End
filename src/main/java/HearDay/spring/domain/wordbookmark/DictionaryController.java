package HearDay.spring.domain.wordbookmark;

import HearDay.spring.common.dto.response.CommonApiResponse;
import HearDay.spring.domain.wordbookmark.service.DictionaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dictionary")
@RequiredArgsConstructor
@Tag(name = "Dictionary", description = "표준국어대사전 API")
public class DictionaryController {

    private final DictionaryService dictionaryService;

    @GetMapping("/search")
    @Operation(summary = "단어 검색", description = "표준국어대사전에서 단어를 검색합니다")
    public ResponseEntity<CommonApiResponse<List<String>>> searchWord(
            @Parameter(description = "검색할 단어") @RequestParam String word
    ) {
        List<String> results = dictionaryService.searchWord(word);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonApiResponse.success(results));
    }
}
