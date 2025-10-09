package HearDay.spring.domain.user.service.CustomUserDetailsService;

import HearDay.spring.domain.user.entity.User;
import HearDay.spring.domain.user.exception.UserException;
import HearDay.spring.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UserException.UserNotFoundException {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UserException.UserNotFoundException("User not found"));

        return new CustomUserDetails(user); // 커스텀 UserDetails 반환
    }
}
