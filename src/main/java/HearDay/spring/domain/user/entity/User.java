package HearDay.spring.domain.user.entity;

import HearDay.spring.common.entity.BaseEntity;
import HearDay.spring.common.enums.AlarmDayType;
import HearDay.spring.common.enums.CategoryEnum;
import HearDay.spring.common.enums.Gender;
import HearDay.spring.domain.discussion.entity.Discussion;
import HearDay.spring.domain.wordbookmark.entity.UserWordBookmark;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String nickname;

    private String password;

    @Column(nullable = false)
    private String email;

    private String phone;

    @Column(nullable = false)
    private Integer point;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Integer age;

    @ElementCollection(targetClass = CategoryEnum.class)
    @CollectionTable(
            name = "user_category", // 별도 테이블 생성
            joinColumns = @JoinColumn(name = "user_id") // User 엔티티와 매핑
            )
    @Enumerated(EnumType.STRING)
    private List<CategoryEnum> userCategory;

    @Column(name = "alarm_hour")
    private Integer alarmHour; // 알람 시간 (0-23)

    @Column(name = "alarm_minute")
    private Integer alarmMinute; // 알람 분 (0-59)

    @Enumerated(EnumType.STRING)
    @Column(name = "alarm_day_type")
    private AlarmDayType alarmDayType; // 알람 요일 타입 (매일/평일/주말)

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserWordBookmark> userWordBookmarkList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Discussion> discussionList = new ArrayList<>();

    public void updateCategory(List<CategoryEnum> category) {
        this.userCategory = category;
    }

    public void changePassword(String newPassword, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(newPassword);
    }

    public void updateGenderAndAge(Gender gender, Integer age) {
        this.gender = gender;
        this.age = age;
    }

    public void updateAlarmTime(Integer hour, Integer minute, AlarmDayType dayType) {
        this.alarmHour = hour;
        this.alarmMinute = minute;
        this.alarmDayType = dayType;
    }
}
