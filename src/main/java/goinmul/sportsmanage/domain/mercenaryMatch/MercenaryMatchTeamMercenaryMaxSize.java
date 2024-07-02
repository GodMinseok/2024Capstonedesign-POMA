package goinmul.sportsmanage.domain.mercenaryMatch;


import goinmul.sportsmanage.domain.teamMatch.Team;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MercenaryMatchTeamMercenaryMaxSize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mercenary_size_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "mercenary_match_id")
    private MercenaryMatch mercenaryMatch;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    private Integer maxSize;

    //구장예약할때 mappedby로 실행되는 메서드
    public static MercenaryMatchTeamMercenaryMaxSize createMercenaryTeam(Team team, Integer maxSize){
        MercenaryMatchTeamMercenaryMaxSize mercenaryTeam = new MercenaryMatchTeamMercenaryMaxSize();
        mercenaryTeam.setTeam(team);
        mercenaryTeam.setMaxSize(maxSize);
        return mercenaryTeam;
    }

    public static MercenaryMatchTeamMercenaryMaxSize createMercenaryTeam(MercenaryMatch mercenaryMatch, Team team, Integer maxSize){
        MercenaryMatchTeamMercenaryMaxSize mercenaryTeam = new MercenaryMatchTeamMercenaryMaxSize();
        mercenaryTeam.setMercenaryMatch(mercenaryMatch);
        mercenaryTeam.setTeam(team);
        mercenaryTeam.setMaxSize(maxSize);
        return mercenaryTeam;
    }


}

