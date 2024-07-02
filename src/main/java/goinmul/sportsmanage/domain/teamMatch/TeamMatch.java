package goinmul.sportsmanage.domain.teamMatch;



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
public class TeamMatch {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_match_id")
    private Long id;

    private Integer maxSize; //nowsize는 SocialUser 데이터 개수로 대체함

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Enumerated(EnumType.STRING)
    private Sports sports;

    @OneToMany(mappedBy = "teamMatch", cascade = CascadeType.ALL)
    private List<TeamUser> teamUsers = new ArrayList<>();

    //팀 매치 조회할때 사용
    @Transient
    List<Team> teamList = new ArrayList<>();

    //두번 나눠서 조인한걸 teamUsers에 못 넣기때문에 같은 변수 만듬
    @Transient
    private List<TeamUser> transientTeamUsers = new ArrayList<>();

    public static TeamMatch createTeamMatch(){
        return new TeamMatch();
    }
    public static TeamMatch createTeamMatch(int maxSize, Gender gender, Sports sports, Reservation reservation, List<TeamUser> teamUsers){
        TeamMatch teamMatch = new TeamMatch();
        teamMatch.setMaxSize(maxSize);
        teamMatch.setGender(gender);
        teamMatch.setSports(sports);
        teamMatch.setReservation(reservation);

        for (TeamUser teamUser : teamUsers) {
            teamMatch.addTeamUser(teamUser);
        }

        return teamMatch;
    }

    private void addTeamUser(TeamUser teamUser){
        teamUsers.add(teamUser);
        teamUser.setTeamMatch(this);

    }



}
