package HearDay.spring.domain.discussion.service;

import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class TextToSpeechService {
    public String synthesizeToBase64(String text) {
        try (TextToSpeechClient client = TextToSpeechClient.create()) {

            // TTS 입력
            SynthesisInput input = SynthesisInput.newBuilder()
                    .setText(text)
                    .build();

            // 음성 선택
            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode("ko-KR")
                    .setSsmlGender(SsmlVoiceGender.NEUTRAL)
                    .build();

            // 오디오 설정
            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.LINEAR16) // WAV
                    .build();

            // TTS 요청
            SynthesizeSpeechResponse response = client.synthesizeSpeech(input, voice, audioConfig);

            // byte -> Base64
            ByteString audioContent = response.getAudioContent();
            String base64Audio = Base64.getEncoder().encodeToString(audioContent.toByteArray());

            // data URI로 반환 (프론트에서 <audio> 태그로 바로 재생 가능)
            return "data:audio/wav;base64," + base64Audio;

        } catch (Exception e) {
            throw new RuntimeException("TTS 실패", e);
        }
    }
}
