package HearDay.spring.domain.discussion.entity;

import HearDay.spring.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DisscussionContent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer byUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discussion_id")
    private Discussion discussion;
}
