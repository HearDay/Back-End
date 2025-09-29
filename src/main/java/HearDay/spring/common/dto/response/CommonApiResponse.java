package HearDay.spring.common.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
* 공통 API 응답 포맷
* success: 요청 성공 여부
* message: 추가적인 메시지 (성공/실패 시 모두 사용 가능)
* data: 실제 응답 데이터 (성공 시)
* errorCode: 에러 코드 (실패 시)
* ResponseEntity와 함께 사용하여 HTTP 상태 코드도 함께 반환
* */


@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonApiResponse<T> {
    private final boolean success;
    private final String message;
    private final T data;
    private final String errorCode;

    // 성공 응답
    public static <T> CommonApiResponse<T> success(T data) {
        return new CommonApiResponse<>(true, null, data, null);
    }

    // 성공 + 메시지
    public static <T> CommonApiResponse<T> success(String message, T data) {
        return new CommonApiResponse<>(true, message, data, null);
    }

    // 실패 응답
    public static <T> CommonApiResponse<T> error(String message, String errorCode) {
        return new CommonApiResponse<>(false, message, null, errorCode);
    }
}