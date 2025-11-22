package HearDay.spring.common.enums;

public enum AlarmDayType {
    EVERYDAY("매일"),
    WEEKDAY("평일"),
    WEEKEND("주말");

    private final String description;

    AlarmDayType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
