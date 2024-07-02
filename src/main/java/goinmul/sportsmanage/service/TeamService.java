package goinmul.sportsmanage.service;



import goinmul.sportsmanage.domain.teamMatch.Team;
import goinmul.sportsmanage.repository.TeamRepository;
import goinmul.sportsmanage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public boolean saveTeam(Team team) {
        boolean result = validateDuplicateTeam(team.getName());
        if(result) teamRepository.save(team);
        return result;
    }

    //팀 이름 중복 검증
    private boolean validateDuplicateTeam(String name) {
        Team team = teamRepository.findTeamByName(name);
        boolean result;
        result = (team == null);
        return result;
    }


}
