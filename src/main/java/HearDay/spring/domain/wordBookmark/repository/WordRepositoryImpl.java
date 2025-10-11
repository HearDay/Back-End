package HearDay.spring.domain.wordBookmark.repository;

import HearDay.spring.domain.user.entity.User;
import HearDay.spring.domain.wordBookmark.dto.response.WordDescriptionResponseDto;
import HearDay.spring.domain.wordBookmark.entity.QUserWordBookmark;
import HearDay.spring.domain.wordBookmark.entity.UserWordBookmark;
import HearDay.spring.domain.wordBookmark.exception.WordException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WordRepositoryImpl implements WordRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public WordDescriptionResponseDto findWordByUserAndWordId(User user, Long wordsId) {
        QUserWordBookmark userWordBookmark = QUserWordBookmark.userWordBookmark;

        UserWordBookmark result = queryFactory
                .selectFrom(userWordBookmark)
                .where(
                        userWordBookmark.id.eq(wordsId),
                        userWordBookmark.user.eq(user)
                )
                .fetchOne();

        if (result == null) {
            throw new WordException.WordNotFoundException(wordsId);
        }

        return new WordDescriptionResponseDto(
                result.getWord(),
                result.getDescription()
        );
    }
}
