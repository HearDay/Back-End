package HearDay.spring.domain.wordBookmark.exception;

import HearDay.spring.common.execption.CommonException;
import HearDay.spring.common.execption.ErrorCode;

public class WordException {

    public static class WordNotFoundException extends CommonException {
        public WordNotFoundException(Long id) {
            super(ErrorCode.WORD_BOOKMARK_NOT_EXISTS, "북마크가 존재하지 않습니다.");
        }
    }

    public static class WordAlreadyBookmarkedException extends CommonException {
        public WordAlreadyBookmarkedException() {
            super(ErrorCode.WORD_ALREADY_BOOKMARKED_TODAY);
        }
    }
}