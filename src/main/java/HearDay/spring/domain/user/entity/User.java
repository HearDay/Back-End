package HearDay.spring.domain.user.entity;

import HearDay.spring.common.entity.BaseEntity;
import HearDay.spring.common.enums.CategoryEnum;
import HearDay.spring.domain.discussion.entity.Discussion;
import HearDay.spring.domain.userarticlebookmark.entity.UserArticleBookmark;
import HearDay.spring.domain.wordbookmark.entity.UserWordBookmark;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String loginId;

    private String password;

    @Column(nullable = false)
    private String email;

    private String phone;

    @Column(nullable = false)
    private Integer level;

    @ElementCollection(targetClass = CategoryEnum.class)
    @CollectionTable(
            name = "user_category",  // 별도 테이블 생성
            joinColumns = @JoinColumn(name = "user_id") // User 엔티티와 매핑
    )
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private List<CategoryEnum> userCategory;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserWordBookmark> userWordBookmarkList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserArticleBookmark> userArticleBookmarkList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Discussion> discussionList = new ArrayList<>();
}
