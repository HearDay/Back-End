package HearDay.spring.domain.articlequiz.service;

import HearDay.spring.domain.articlequiz.dto.ArticleQuizDto;
import HearDay.spring.domain.articlequiz.entity.ArticleQuiz;
import HearDay.spring.domain.articlequiz.entity.ArticleQuizUserResult;
import HearDay.spring.domain.articlequiz.exception.ArticleQuizException;
import HearDay.spring.domain.articlequiz.repository.ArticleQuizRepository;
import HearDay.spring.domain.articlequiz.repository.ArticleQuizUserResultRepository;
import HearDay.spring.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleQuizService {

    private final ArticleQuizRepository articleQuizRepository;
    private final ArticleQuizUserResultRepository articleQuizUserResultRepository;

    public ArticleQuizDto getArticleQuiz(Long articleId, Long userId) {
        ArticleQuiz quiz = articleQuizRepository.findByArticleId(articleId)
                .orElseThrow(() -> new ArticleQuizException.QuizNotFoundException(articleId));

        boolean isSolved = articleQuizUserResultRepository
                .findByArticleQuizIdAndUserId(quiz.getId(), userId)
                .isPresent();

        return ArticleQuizDto.from(quiz, isSolved);
    }

    @Transactional
    public void submitQuizAnswer(Long quizId, User user) {
        ArticleQuiz quiz = articleQuizRepository.findById(quizId)
                .orElseThrow(() -> new ArticleQuizException.QuizNotFoundException(quizId));

        articleQuizUserResultRepository.findByArticleQuizIdAndUserId(quizId, user.getId())
                .ifPresent(result -> {
                    throw new ArticleQuizException.QuizAlreadySolvedException();
                });

        ArticleQuizUserResult result = ArticleQuizUserResult.builder()
                .articleQuiz(quiz)
                .user(user)
                .solvedAt(LocalDateTime.now())
                .build();

        articleQuizUserResultRepository.save(result);
    }
}
