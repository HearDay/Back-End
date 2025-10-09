package HearDay.spring.domain;

import HearDay.spring.domain.user.entity.User;
import HearDay.spring.global.annotation.AuthUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthUserTestController {

    @GetMapping("/test/auth-user")
    public String getAuthUserLoginId(@AuthUser User user) {
        if (user == null) {
            return "user is null";
        }
        return user.getLoginId();
    }
}