package HearDay.spring.domain.usercalendar.repository;

import HearDay.spring.domain.user.entity.User;
import HearDay.spring.domain.usercalendar.entity.UserCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserCalendarRepository extends JpaRepository<UserCalendar, Long> {

    boolean existsByUserAndAttendanceDate(User user, LocalDate attendanceDate);

    List<UserCalendar> findAllByUserAndAttendanceDateBetween(
            User user,
            LocalDate startDate,
            LocalDate endDate
    );
}