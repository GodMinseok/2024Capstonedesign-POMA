package goinmul.sportsmanage.service.teamMatch;

import goinmul.sportsmanage.domain.teamMatch.TeamUser;
import goinmul.sportsmanage.repository.teamMatch.TeamUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamUserService {

    private final TeamUserRepository teamUserRepository;

    @Transactional
    public void saveTeamUser(TeamUser teamUser) {
        teamUserRepository.save(teamUser);
    }

    //모아뒀다가 저장할라고 리스트용 저장 메서드 만듬
    @Transactional
    public void saveTeamUsers(List<TeamUser> teamUsers) {
        for (TeamUser teamUser : teamUsers) {
            teamUserRepository.save(teamUser);
        }
    }

    public List<TeamUser> findTeamUsersWithUserAndTeamByTeamMatchIdIn(List<Long> teamMatchIdList) {
        return teamUserRepository.findWithUserAndTeamByTeamMatchIdIn(teamMatchIdList);
    }

}
