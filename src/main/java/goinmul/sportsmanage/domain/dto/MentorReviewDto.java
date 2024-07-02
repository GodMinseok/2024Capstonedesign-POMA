package goinmul.sportsmanage.domain.dto;

import goinmul.sportsmanage.domain.MentorReview;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MentorReviewDto {

    private MentorReview mentorReview;
    private Double avg;
    private Long count;

    public MentorReviewDto(MentorReview mentorReview, Double avg, Long count) {
        this.mentorReview = mentorReview;
        this.avg = avg;
        this.count = count;
    }

}
