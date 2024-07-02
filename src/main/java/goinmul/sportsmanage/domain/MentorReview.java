package goinmul.sportsmanage.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MentorReview {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mentor_mentee_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "mentor_id")
    private User mentor;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "mentee_id")
    private User mentee;

    private Integer score;

    private String description;

    public static MentorReview createMentorMentee(User mentor, User mentee, Integer score, String description){
        MentorReview mentorMentee = new MentorReview();
        mentorMentee.setMentor(mentor);
        mentorMentee.setMentee(mentee);
        mentorMentee.setScore(score);
        mentorMentee.setDescription(description);
        return mentorMentee;
    }


}
