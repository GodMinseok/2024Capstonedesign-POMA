package goinmul.sportsmanage.service.teamMatch;

import goinmul.sportsmanage.domain.Gender;
import goinmul.sportsmanage.domain.Reservation;
import goinmul.sportsmanage.domain.Sports;
import goinmul.sportsmanage.domain.dto.TeamMatchDto;
import goinmul.sportsmanage.domain.teamMatch.Team;
import goinmul.sportsmanage.domain.teamMatch.TeamMatch;
import goinmul.sportsmanage.domain.teamMatch.TeamUser;
import goinmul.sportsmanage.repository.ReservationRepository;
import goinmul.sportsmanage.repository.teamMatch.TeamMatchRepository;
import goinmul.sportsmanage.repository.teamMatch.TeamUserRepository;
import goinmul.sportsmanage.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TeamMatchService {

    private final TeamMatchRepository teamMatchRepository;
    private final TeamUserRepository teamUserRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public void saveTeamMatch(TeamMatch teamMatch) {
        teamMatchRepository.save(teamMatch);
    }

    //팀 매치 멤버 조회
    public TeamMatch findOneWithTeamUserAndUser(Long teamMatchId) {
        TeamMatch teamMatch = teamMatchRepository.findOne(teamMatchId);
        List<TeamUser> teamUsers = teamUserRepository.findWithUserByTeamMatchIdAndGender(teamMatchId, teamMatch.getGender());
        teamMatch.getTransientTeamUsers().addAll(teamUsers);
        return teamMatch;
    }

    public List<TeamMatchDto> getTeamMatches(LocalDate date, String location, Gender gender, Sports sports) {

        List<TeamMatch> teamMatches = teamMatchRepository.findWithReservationAndGroundByDateAndLocationAndGender(date, location, gender, sports);

        List<TeamMatchDto> teamMatchesDto = teamMatches.stream()
                .map(t -> new TeamMatchDto(t))
                .collect(Collectors.toList());

        List<Long> teamMatchIdList = new ArrayList<>();
        for (TeamMatch teamMatch : teamMatches) {
            teamMatchIdList.add(teamMatch.getId());
        }

        List<TeamUser> teamUsers = teamUserRepository.findWithUserAndTeamByTeamMatchIdIn(teamMatchIdList);

        //teamMatchesDto에 teamUsers할당
        for (TeamMatchDto teamMatchDto : teamMatchesDto) {
            for (TeamUser teamUser : teamUsers) {
                if (teamMatchDto.getId().equals(teamUser.getTeamMatch().getId())) {
                    teamMatchDto.getTeamUsers().add(teamUser);
                }
            }
        }

        Set<Long> teamIdList = new HashSet<>();

        //teamMatchesDto에 팀 리스트 할당
        for (TeamMatchDto teamMatchDto : teamMatchesDto) {
            for (TeamUser teamUser : teamMatchDto.getTeamUsers()) {
                Team team = teamUser.getTeam();
                if (!teamIdList.contains(team.getId())) {
                    teamIdList.add(team.getId());
                    teamMatchDto.getTeams().add(team);
                }
            }
            teamIdList.clear();
        }

        return teamMatchesDto;

    }


}
