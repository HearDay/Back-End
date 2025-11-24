package HearDay.spring.domain.discussion.service;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import com.google.protobuf.ByteString;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class SpeechToTextService {

    @Value("${google.credentials}")
    private String googleCredentialsPath;

    private SpeechClient speechClient;

    @PostConstruct
    public void init() throws IOException {
        Path path = Paths.get(googleCredentialsPath);  // 절대 경로
        try (InputStream stream = Files.newInputStream(path)) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(stream);
            SpeechSettings settings = SpeechSettings.newBuilder()
                    .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                    .build();
            speechClient = SpeechClient.create(settings);
        }
    }

    public String transcribe(MultipartFile audioFile, int rateHertz) {
        try {
            File originalFile = File.createTempFile("input", ".tmp");
            audioFile.transferTo(originalFile);

            File wavFile = convertToWav(originalFile);

            byte[] audioBytes = Files.readAllBytes(wavFile.toPath());

            RecognitionAudio audio = RecognitionAudio.newBuilder()
                    .setContent(ByteString.copyFrom(audioBytes))
                    .build();

            RecognitionConfig config = RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                    .setLanguageCode("ko-KR")
                    .setSampleRateHertz(rateHertz)
                    .build();

            RecognizeResponse response = speechClient.recognize(config, audio);

            StringBuilder sb = new StringBuilder();
            for (SpeechRecognitionResult result : response.getResultsList()) {
                sb.append(result.getAlternatives(0).getTranscript());
            }

            return sb.toString();

        } catch (Exception e) {
            throw new RuntimeException("STT 변환 실패", e);
        }
    }

    // ffmpeg 변환
    private File convertToWav(File inputFile) throws IOException, InterruptedException {
        File outputFile = new File(inputFile.getParent(), "converted.wav");

        ProcessBuilder pb = new ProcessBuilder(
                "ffmpeg",
                "-y",                    // 덮어쓰기
                "-i", inputFile.getAbsolutePath(), // 입력 파일
                "-ac", "1",              // mono
                "-ar", "16000",          // 16kHz
                "-f", "wav",             // WAV 포맷
                outputFile.getAbsolutePath()
        );

        pb.redirectErrorStream(true);
        Process process = pb.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException("ffmpeg 변환 실패 (exitCode=" + exitCode + ")");
        }

        return outputFile;
    }
}
