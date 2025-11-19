package HearDay.spring.domain.articlequiz.exception;

import HearDay.spring.common.execption.CommonException;
import HearDay.spring.common.execption.ErrorCode;

public class ArticleQuizException {

    public static class QuizNotFoundException extends CommonException {
        public QuizNotFoundException(Long id) {
            super(ErrorCode.ARTICLE_QUIZ_NOT_FOUND, "퀴즈를 찾을 수 없습니다. ID: " + id);
        }
    }

    public static class QuizAlreadySolvedException extends CommonException {
        public QuizAlreadySolvedException() {
            super(ErrorCode.ARTICLE_QUIZ_ALREADY_SOLVED);
        }
    }
}
