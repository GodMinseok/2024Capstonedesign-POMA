package goinmul.sportsmanage.domain.mercenaryMatch;


import goinmul.sportsmanage.domain.User;
import goinmul.sportsmanage.domain.teamMatch.Team;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class TeamWithMercenary {

    private MercenaryMatch mercenaryMatch;
    private Team team;
    private Integer teamMercenaryMaxSize;
    private List<MercenaryMatchUser> mercenaries = new ArrayList<>();
    //private List<User> users = new ArrayList<>();

    public static TeamWithMercenary createTeamWithMercenary(){
        return new TeamWithMercenary();
    }

    public static TeamWithMercenary createTeamWithMercenary(Team team, Integer teamMercenaryMaxSize){
        TeamWithMercenary teams = new TeamWithMercenary();
        teams.setTeam(team);
        teams.setTeamMercenaryMaxSize(teamMercenaryMaxSize);
        return teams;
    }

    public static TeamWithMercenary createTeamWithMercenary(Team team){
        TeamWithMercenary teams = new TeamWithMercenary();
        teams.setTeam(team);
        return teams;
    }

    public static TeamWithMercenary createTeamWithMercenary(Team team, MercenaryMatchUser mercenary){
        TeamWithMercenary teams = new TeamWithMercenary();
        teams.setTeam(team);
        teams.getMercenaries().add(mercenary);
        return teams;
    }

    public static TeamWithMercenary createTeamWithMercenary(MercenaryMatch mercenaryMatch, Team team, Integer teamMercenaryMaxSize, MercenaryMatchUser mercenaryMatchUser){
        TeamWithMercenary teams = new TeamWithMercenary();
        teams.setTeam(team);
        teams.setTeamMercenaryMaxSize(teamMercenaryMaxSize);
        teams.getMercenaries().add(mercenaryMatchUser);
        return teams;
    }

    public static TeamWithMercenary createTeamWithMercenary(Team team, Integer teamMercenaryMaxSize, MercenaryMatchUser mercenaryMatchUser){
        TeamWithMercenary teams = new TeamWithMercenary();
        teams.setTeam(team);
        teams.setTeamMercenaryMaxSize(teamMercenaryMaxSize);
        teams.getMercenaries().add(mercenaryMatchUser);
        return teams;
    }

    public static TeamWithMercenary createTeamWithMercenary(MercenaryMatch mercenaryMatch, Team team, MercenaryMatchUser mercenary){
        TeamWithMercenary teamWithMercenary = new TeamWithMercenary();
        teamWithMercenary.setMercenaryMatch(mercenaryMatch);
        teamWithMercenary.setTeam(team);
        teamWithMercenary.mercenaries.add(mercenary);
        return teamWithMercenary;
    }

    public void changeMaxSize(Integer teamMercenaryMaxSize){
        this.teamMercenaryMaxSize = teamMercenaryMaxSize;
    }

}
