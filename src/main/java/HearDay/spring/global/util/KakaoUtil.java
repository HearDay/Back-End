package HearDay.spring.global.util;

import HearDay.spring.domain.user.dto.request.KakaoRequestDto;
import HearDay.spring.domain.user.dto.response.KakaoResponseDto;
import HearDay.spring.domain.user.exception.UserException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
public class KakaoUtil {

    @Value("${kakao.auth.client}")
    private String client;

    @Value("${kakao.auth.redirect}")
    private String redirect;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final WebClient webClient = WebClient.create();

    public KakaoResponseDto requestToken(String accessCode) {
        String response = webClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", client)
                        .with("redirect_uri", redirect)
                        .with("code", accessCode))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            KakaoResponseDto token = objectMapper.readValue(response, KakaoResponseDto.class);
            log.info("access_token: {}", token.accessToken());
            return token;
        } catch (JsonProcessingException e) {
            log.error("Token parsing error", e);
            throw new UserException.KakaoException();
        }
    }

    public KakaoRequestDto requestProfile(KakaoResponseDto oAuthToken) {
        String response = webClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header("Authorization", "Bearer " + oAuthToken.accessToken())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            return objectMapper.readValue(response, KakaoRequestDto.class);
        } catch (JsonProcessingException e) {
            log.error("Profile parsing error", e);
            throw new UserException.KakaoException();
        }
    }
}