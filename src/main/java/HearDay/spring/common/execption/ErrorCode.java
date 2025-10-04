package HearDay.spring.common.execption;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 공통
    INVALID_INPUT_VALUE(400, "C001", "잘못된 입력값입니다"),
    NOT_FOUND(404, "C002", "리소스를 찾을 수 없습니다"),
    INTERNAL_SERVER_ERROR(500, "C003", "서버 오류가 발생했습니다"),

    // Article 도메인
    ARTICLE_NOT_FOUND(404, "A001", "게시글을 찾을 수 없습니다"),
    ARTICLE_ALREADY_DELETED(400, "A002", "이미 삭제된 게시글입니다"),
    ARTICLE_TITLE_REQUIRED(400, "A003", "게시글 제목은 필수입니다"),

    // User 도메인
    USER_NOT_FOUND(404, "U001", "사용자를 찾을 수 없습니다.."),
    USER_LOGIN_ID_EXIST(400, "U002", "이미 존재하는 아이디입니다."),
    USER_EMAIL_EXIST(400, "U003", "이미 가입된 이메일입니다.");

    private final int status;
    private final String code;
    private final String message;
}
