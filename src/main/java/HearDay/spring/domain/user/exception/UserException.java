package HearDay.spring.domain.user.exception;

import HearDay.spring.common.execption.CommonException;
import HearDay.spring.common.execption.ErrorCode;

public class UserException {

    public static class UserNotFoundException extends CommonException {
        public UserNotFoundException(Long id) {
            super(ErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다. ID: " + id); }
      
        public UserNotFoundException(String email) {
            super(ErrorCode.USER_NOT_FOUND, "해당 이메일의 사용자를 찾을 수 없습니다. email: " + email);
        }

        public UserNotFoundException(String loginId, String password) {
            super(ErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }
    }

    public static class UserEmailAlreadyExistException extends CommonException {
        public UserEmailAlreadyExistException(String email) {
            super(ErrorCode.USER_EMAIL_EXIST, "이미 존재하는 이메일입니다. Email: " + email);
        }
    }

    public static class UserPasswordSameAsOldException extends CommonException {
        public UserPasswordSameAsOldException(String password) {
            super(ErrorCode.USER_PASSWORD_SAME_AS_OLD, password);
        }
    }

    public static class KakaoException extends CommonException {
        public KakaoException() {
            super(ErrorCode.PARSING_ERROR);
        }
    }

    public static class RefreshTokenException extends CommonException {
        public RefreshTokenException() {
            super(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    public static class EmailCodeExpiredException extends CommonException {
        public EmailCodeExpiredException(String email) {
            super(ErrorCode.USER_EMAIL_ERROR, "인증 코드가 만료되었습니다: " + email);
        }
    }

    public static class EmailCodeMismatchException extends CommonException {
        public EmailCodeMismatchException(String email) {
            super(ErrorCode.USER_EMAIL_ERROR, "인증 코드가 일치하지 않습니다: " + email);
        }
    }
}
