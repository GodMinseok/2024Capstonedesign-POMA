package goinmul.sportsmanage.domain.dto;


import goinmul.sportsmanage.domain.Ground;
import goinmul.sportsmanage.domain.socialMatch.SocialMatch;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationWithGroundWithSocialMatchDto {

    private Ground ground; //groundId는 안필요함
    private SocialMatch socialMatch;
    private LocalDate reservationYmd;
    private int reservationTime;

}
