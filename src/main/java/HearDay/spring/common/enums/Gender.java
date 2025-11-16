package HearDay.spring.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {
    M("남성"),
    F("여성"),
    UNKNOWN("미공개");

    private final String description;
}
