package HearDay.spring.domain.article.exception;

import HearDay.spring.common.execption.CommonException;
import HearDay.spring.common.execption.ErrorCode;

public class ArticleException {

    public static class ArticleNotFoundException extends CommonException {
        public ArticleNotFoundException(Long id) {
            super(ErrorCode.ARTICLE_NOT_FOUND, "게시글을 찾을 수 없습니다. ID: " + id);
        }
    }
}
