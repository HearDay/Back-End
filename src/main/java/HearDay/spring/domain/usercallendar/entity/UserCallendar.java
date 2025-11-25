package HearDay.spring.domain.usercallendar.entity;

import HearDay.spring.common.entity.BaseEntity;
import HearDay.spring.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
        name = "user_callendar",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "attendance_date"})
        }
)
public class UserCallendar extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;
}
