package HearDay.spring.domain.user.service;

import HearDay.spring.domain.user.entity.User;

public interface UserQueryService {
    void checkId(String userLoginId);
    User getUserEntity(Long userId);
}
