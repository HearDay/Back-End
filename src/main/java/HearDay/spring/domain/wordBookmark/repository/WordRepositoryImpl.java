package HearDay.spring.domain.wordBookmark.repository;

import HearDay.spring.domain.user.entity.User;
import HearDay.spring.domain.wordBookmark.dto.response.WordCountInfoResponseDto;
import HearDay.spring.domain.wordBookmark.dto.response.WordDescriptionResponseDto;
import HearDay.spring.domain.wordBookmark.entity.QUserWordBookmark;
import HearDay.spring.domain.wordBookmark.entity.UserWordBookmark;
import HearDay.spring.domain.wordBookmark.exception.WordException;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public List<WordCountInfoResponseDto> findWordCountsByMonth(User user, int year, int month) {
        QUserWordBookmark w = QUserWordBookmark.userWordBookmark;

        LocalDateTime startOfMonth = LocalDate.of(year, month, 1).atStartOfDay();
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1);

        // DB에서 날짜별 count 가져오기
        List<Tuple> results = queryFactory
                .select(w.createdAt, w.count())
                .from(w)
                .where(w.user.eq(user)
                        .and(w.createdAt.between(startOfMonth, endOfMonth)))
                .groupBy(w.createdAt)
                .fetch();

        // Tuple -> Map<LocalDate, Long>
        Map<LocalDate, Long> countMap = results.stream()
                .collect(Collectors.toMap(
                        t -> t.get(w.createdAt).toLocalDate(),
                        t -> t.get(w.count()),
                        Long::sum
                ));

        // 월 전체 날짜 채우기
        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());

        List<WordCountInfoResponseDto> dtoList = new ArrayList<>();
        for (LocalDate date = firstDay; !date.isAfter(lastDay); date = date.plusDays(1)) {
            long count = countMap.getOrDefault(date, 0L);
            dtoList.add(new WordCountInfoResponseDto(date, count));
        }

        return dtoList;
    }
}
