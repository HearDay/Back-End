package HearDay.spring.domain.discussion.exception;

import HearDay.spring.common.execption.CommonException;
import HearDay.spring.common.execption.ErrorCode;

public class DiscussionException {
    public static class DiscussionNotFoundException extends CommonException {
        public DiscussionNotFoundException(Long discussionId) {
            super(ErrorCode.DISCUSSION_NOT_EXISTS, "해당 아이디의 토론을 찾을 수 없습니다." + discussionId);
        }
    }
}
