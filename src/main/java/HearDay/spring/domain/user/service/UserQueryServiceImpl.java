package HearDay.spring.domain.user.service;

import HearDay.spring.domain.article.entity.Article;
import HearDay.spring.domain.article.repository.ArticleRepository;
import HearDay.spring.domain.user.dto.response.HomeResponseDto;
import HearDay.spring.domain.user.entity.User;
import HearDay.spring.domain.user.exception.UserException;
import HearDay.spring.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    @Override
    public void checkId(String userLoginId) {
//        if (userRepository.findByLoginId(userLoginId).isPresent()) {
//            throw new UserException.UserLoginIdAlreadyExistException(userLoginId);
//        }
    }

    @Override
    public User getUserEntity(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserException.UserNotFoundException(userId));
    }

    @Override
    public HomeResponseDto getHomeInformation(User user) {
        Optional<Article> recentUpdatedTime = articleRepository.findTopByOrderByCreatedAtDesc();

        String formattedDate = recentUpdatedTime
                .map(article -> article.getCreatedAt().format(
                        DateTimeFormatter.ofPattern("M월 d일 HH:mm", Locale.KOREAN)
                ))
                .orElse("데이터 없음");

        return new HomeResponseDto(
                user.getLevel(),
                user.getNickname(),
                formattedDate
        );
    }
}
