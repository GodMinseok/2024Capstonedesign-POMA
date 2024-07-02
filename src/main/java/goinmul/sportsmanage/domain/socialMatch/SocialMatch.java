package goinmul.sportsmanage.domain.socialMatch;


import goinmul.sportsmanage.domain.*;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;

@Entity @DynamicInsert
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialMatch {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "social_match_id")
    private Long id;

    private Integer maxSize;


    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Enumerated(EnumType.STRING)
    private Sports sports;

    @OneToMany(mappedBy = "socialMatch", cascade = ALL)
    private List<SocialUser> socialUsers = new ArrayList<>();


    public static SocialMatch createSocialMatch(int maxSize, Gender gender, Sports sports, SocialUser socialUser, Reservation reservation){
        SocialMatch socialMatch = new SocialMatch();
        socialMatch.setMaxSize(maxSize);
        socialMatch.setGender(gender);
        socialMatch.setSports(sports);
        socialMatch.setReservation(reservation);
        socialMatch.addSocialUser(socialUser);

        return socialMatch;
    }

    private void addSocialUser(SocialUser socialUser){
            socialUsers.add(socialUser);
            socialUser.setSocialMatch(this);

    }

}
