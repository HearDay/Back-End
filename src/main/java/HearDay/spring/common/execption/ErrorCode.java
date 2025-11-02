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
    USER_NOT_FOUND(404, "U001", "사용자를 찾을 수 없습니다."),
    USER_LOGIN_ID_EXIST(400, "U002", "이미 존재하는 아이디입니다."),
    USER_EMAIL_EXIST(400, "U003", "이미 가입된 이메일입니다."),
    USER_PASSWORD_SAME_AS_OLD(400, "U004", "새 비밀번호는 이번 비밀번호와 같지 않아야 합니다."),
    PARSING_ERROR(400, "U005", "카카오 로그인 오류"),

    // JWT
    INVALID_TOKEN(404, "J001", "d"),

    // UserArticleBookmark 도메인
    ARTICLE_BOOKMARK_ALREADY(400, "UA001", "이미 북마크한 게시글입니다."),
    ARTICLE_BOOKMARK_NOT_EXISTS(400, "UA002", "북마크가 존재하지 않습니다."),

    // UserWordBookmark 도메인
    WORD_BOOKMARK_NOT_EXISTS(400, "UW001", "북마크가 존재하지 않습니다."),
    WORD_ALREADY_BOOKMARKED_TODAY(400, "UW002", "오늘 이미 같은 단어를 저장했습니다.");

    private final int status;
    private final String code;
    private final String message;
}
