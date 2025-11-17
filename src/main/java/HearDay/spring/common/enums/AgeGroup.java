package HearDay.spring.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AgeGroup {
    TEENS("10대", 10, 19),
    TWENTIES("20대", 20, 29),
    THIRTIES("30대", 30, 39),
    FORTIES("40대", 40, 49),
    FIFTIES("50대", 50, 59),
    SIXTIES_PLUS("60대 이상", 60, 999);

    private final String description;
    private final int minAge;
    private final int maxAge;

    public static AgeGroup fromAge(Integer age) {
        if (age == null) {
            return null;
        }
        
        for (AgeGroup ageGroup : values()) {
            if (age >= ageGroup.minAge && age <= ageGroup.maxAge) {
                return ageGroup;
            }
        }
        
        // 10세 미만은 TEENS로, 999세 초과는 SIXTIES_PLUS로
        if (age < 10) {
            return TEENS;
        }
        return SIXTIES_PLUS;
    }
}
