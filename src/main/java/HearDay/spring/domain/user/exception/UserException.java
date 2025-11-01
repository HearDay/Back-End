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

    public static class UserLoginIdAlreadyExistException extends CommonException {
        public UserLoginIdAlreadyExistException(String loginId) {
            super(ErrorCode.USER_LOGIN_ID_EXIST, "이미 존재하는 아이디입니다. LoginId: " + loginId); }
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

    public static class KakoException extends CommonException {
        public KakoException() {
            super(ErrorCode.PARSING_ERROR);
        }
    }
}
