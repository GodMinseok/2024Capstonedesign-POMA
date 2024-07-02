package goinmul.sportsmanage.domain.dto;



import goinmul.sportsmanage.domain.Gender;
import goinmul.sportsmanage.domain.Reservation;
import goinmul.sportsmanage.domain.Sports;
import goinmul.sportsmanage.domain.teamMatch.Team;
import goinmul.sportsmanage.domain.teamMatch.TeamMatch;
import goinmul.sportsmanage.domain.teamMatch.TeamUser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamMatchDto {

    private Long id;

    private Integer maxSize; //nowsize는 SocialUser 데이터 개수로 대체함

    private Gender gender;

    private Reservation reservation;

    private Sports sports;

    private List<TeamUser> teamUsers = new ArrayList<>();

    private List<Team> teams = new ArrayList<>();

    public TeamMatchDto(TeamMatch teamMatch) {
        id = teamMatch.getId();
        maxSize = teamMatch.getMaxSize();
        gender = teamMatch.getGender();
        reservation = teamMatch.getReservation();
        sports = teamMatch.getSports();
        teamUsers = teamMatch.getTeamUsers();
    }
}
