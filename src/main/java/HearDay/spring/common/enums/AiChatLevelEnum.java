package HearDay.spring.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AiChatLevelEnum {
    BEGINNER("beginner"),
    INTERMEDIATE("intermediate"),
    ADVANCED("advanced");

    private final String value;

    AiChatLevelEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
