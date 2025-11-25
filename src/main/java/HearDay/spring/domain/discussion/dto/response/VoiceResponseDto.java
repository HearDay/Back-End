package HearDay.spring.domain.discussion.dto.response;

public record VoiceResponseDto(
        String reply,
        Long discussionId,
        String title
) {
}
