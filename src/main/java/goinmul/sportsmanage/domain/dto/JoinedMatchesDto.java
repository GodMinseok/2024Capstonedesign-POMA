package goinmul.sportsmanage.domain.dto;

import goinmul.sportsmanage.domain.mercenaryMatch.MercenaryMatchUser;
import goinmul.sportsmanage.domain.socialMatch.SocialUser;
import goinmul.sportsmanage.domain.teamMatch.TeamUser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JoinedMatchesDto {

    private List<SocialUser> socialUsers = new ArrayList<>();
    private List<TeamUser> teamUsers = new ArrayList<>();
    private List<MercenaryMatchUser> mercenaryMatchUsers = new ArrayList<>();

    public static JoinedMatchesDto createJoinedMatchesDto(List<SocialUser> socialUsers, List<TeamUser> teamUsers, List<MercenaryMatchUser> mercenaryMatchUsers){
        JoinedMatchesDto joinedMatchesDto = new JoinedMatchesDto();
        joinedMatchesDto.getSocialUsers().addAll(socialUsers);
        joinedMatchesDto.getTeamUsers().addAll(teamUsers);
        joinedMatchesDto.getMercenaryMatchUsers().addAll(mercenaryMatchUsers);
        return joinedMatchesDto;
    }
}
