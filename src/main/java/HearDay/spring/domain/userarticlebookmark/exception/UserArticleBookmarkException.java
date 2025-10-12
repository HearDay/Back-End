package HearDay.spring.domain.userarticlebookmark.exception;

import HearDay.spring.common.execption.CommonException;
import HearDay.spring.common.execption.ErrorCode;

public class UserArticleBookmarkException {

    public static class ArticleAlreadyBookmarkException extends CommonException {
        public ArticleAlreadyBookmarkException(Long id) {
            super(ErrorCode.ARTICLE_BOOKMARK_ALREADY, "이미 북마크한 게시글입니다. ID: " + id);
        }
    }

    public static class ArticleBookmarkNotExistsException extends CommonException {
        public ArticleBookmarkNotExistsException(Long id) {
            super(ErrorCode.ARTICLE_BOOKMARK_NOT_EXISTS, "삭제할 북마크가 존재하지 않습니다.: " + id);
        }
    }
}
