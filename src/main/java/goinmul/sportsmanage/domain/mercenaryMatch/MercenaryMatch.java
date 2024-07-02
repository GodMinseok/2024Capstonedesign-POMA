package goinmul.sportsmanage.domain.mercenaryMatch;


import goinmul.sportsmanage.domain.Gender;
import goinmul.sportsmanage.domain.Reservation;
import goinmul.sportsmanage.domain.Sports;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MercenaryMatch {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mercenary_match_id")
    private Long id;

    private Integer maxSize;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Enumerated(EnumType.STRING)
    private Sports sports;

    @OneToMany(mappedBy = "mercenaryMatch", cascade = CascadeType.ALL)
    private List<MercenaryMatchUser> mercenaryMatchUsers = new ArrayList<>();

    @OneToMany(mappedBy = "mercenaryMatch", cascade = CascadeType.ALL)
    private List<MercenaryMatchTeamMercenaryMaxSize> mercenaryMaxSizes = new ArrayList<>();

    @Transient
    List<TeamWithMercenary> teamsWithMercenary = new ArrayList<>();

    public static MercenaryMatch createMercenaryMatch(Long mercenaryMatchId){
        MercenaryMatch mercenaryMatch = new MercenaryMatch();
        mercenaryMatch.setId(mercenaryMatchId);
        return mercenaryMatch;
    }

    public static MercenaryMatch createMercenaryMatch(int maxSize, Gender gender, Sports sports, Reservation reservation, List<MercenaryMatchUser> mercenaryUsers,
                                                      MercenaryMatchTeamMercenaryMaxSize mercenaryMaxSize){
        MercenaryMatch mercenaryMatch = new MercenaryMatch();
        mercenaryMatch.setMaxSize(maxSize);
        mercenaryMatch.setGender(gender);
        mercenaryMatch.setSports(sports);
        mercenaryMatch.setReservation(reservation);

        for (MercenaryMatchUser mercenaryUser : mercenaryUsers) {
            mercenaryMatch.addTeamUser(mercenaryUser);
        }

        mercenaryMatch.addMercenaryMaxSize(mercenaryMaxSize);
        return mercenaryMatch;
    }

    private void addTeamUser(MercenaryMatchUser mercenaryUser){
        mercenaryMatchUsers.add(mercenaryUser);
        mercenaryUser.setMercenaryMatch(this);
    }

    private void addMercenaryMaxSize(MercenaryMatchTeamMercenaryMaxSize mercenaryMaxSize){
        mercenaryMaxSizes.add(mercenaryMaxSize);
        mercenaryMaxSize.setMercenaryMatch(this);
    }

}
