package HearDay.spring.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AiChatModeEnum {
    OPEN("open_question"),
    FOLLOWUP("followup");

    private final String value;

    AiChatModeEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
