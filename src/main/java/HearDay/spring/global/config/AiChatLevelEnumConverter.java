package HearDay.spring.global.config;

import HearDay.spring.common.enums.AiChatLevelEnum;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

@Component
public class AiChatLevelEnumConverter implements Converter<String, AiChatLevelEnum> {

    @Override
    public AiChatLevelEnum convert(String value) {
        if (value == null) {
            return null;
        }

        for (AiChatLevelEnum level : AiChatLevelEnum.values()) {
            if (level.getValue().equalsIgnoreCase(value)) {
                return level;
            }
        }

        throw new IllegalArgumentException("Invalid AI chat level: " + value);
    }
}