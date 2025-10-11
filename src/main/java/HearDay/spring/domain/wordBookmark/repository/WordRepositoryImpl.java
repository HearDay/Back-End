package HearDay.spring.domain.wordBookmark.repository;

import HearDay.spring.domain.user.entity.User;
import HearDay.spring.domain.wordBookmark.dto.response.WordByDateResponseDto;
import HearDay.spring.domain.wordBookmark.dto.response.WordDescriptionResponseDto;
import HearDay.spring.domain.wordBookmark.entity.QUserWordBookmark;
import HearDay.spring.domain.wordBookmark.entity.UserWordBookmark;
import HearDay.spring.domain.wordBookmark.exception.WordException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class WordRepositoryImpl implements WordRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
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

    @Override
    public boolean checkTodayWordBookmark(User user, String word) {
        QUserWordBookmark userWordBookmark = QUserWordBookmark.userWordBookmark;

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

        boolean exists = queryFactory
                .selectOne()
                .from(userWordBookmark)
                .where(
                        userWordBookmark.user.eq(user),
                        userWordBookmark.word.eq(word),
                        userWordBookmark.createdAt.between(startOfDay, endOfDay)
                )
                .fetchFirst() != null;

        return exists;
    }

    @Override
    public List<UserWordBookmark> getWordByDates(User user, LocalDate date) {
        QUserWordBookmark userWordBookmark = QUserWordBookmark.userWordBookmark;

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        return queryFactory
                .selectFrom(userWordBookmark)
                .where(
                        userWordBookmark.user.eq(user),
                        userWordBookmark.createdAt.between(startOfDay, endOfDay)
                )
                .fetch();
    }
}
