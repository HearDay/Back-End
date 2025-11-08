package HearDay.spring.domain.user.service;

import HearDay.spring.domain.user.exception.UserException;
import HearDay.spring.global.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final RedisService redisService;

    public void sendMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }

    public boolean verifyCode(String email, String code) {
        String savedCode = redisService.getData("EMAIL_CODE:" + email);
        if (savedCode == null) {
            throw new UserException.EmailCodeExpiredException(email);
        }

        if (!savedCode.equals(code)) {
            throw new UserException.EmailCodeMismatchException(email);
        }

        return true;
    }
}
