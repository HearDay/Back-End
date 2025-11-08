package HearDay.spring.domain.user.service;

import HearDay.spring.global.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisService redisService;

    private static final long REFRESH_TOKEN_EXPIRE = 60 * 60 * 24 * 7; // 7일

    public void saveRefreshToken(String email, String token) {
        redisService.setData("refresh:" + email, token, REFRESH_TOKEN_EXPIRE);
    }

    public String getRefreshToken(String email) {
        return redisService.getData("refresh:" + email);
    }

    public void deleteRefreshToken(Long userId) {
        redisService.deleteData("refresh:" + userId);
    }
}