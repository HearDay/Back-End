package HearDay.spring.domain.usercalendar.service;

import HearDay.spring.domain.user.entity.User;
import HearDay.spring.domain.usercalendar.entity.UserCalendar;
import HearDay.spring.domain.usercalendar.repository.UserCalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserCalendarService {
    private final UserCalendarRepository userCalendarRepository;

    public void checkAttendance(User user) {
        LocalDate today = LocalDate.now();

        if (userCalendarRepository.existsByUserAndAttendanceDate(user, today)) {
            return; // 오늘 이미 출석
        }

        UserCalendar attendance = UserCalendar.builder()
                .user(user)
                .attendanceDate(today)
                .build();

        userCalendarRepository.save(attendance);
    }
}
