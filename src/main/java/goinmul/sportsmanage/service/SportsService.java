package goinmul.sportsmanage.service;


import goinmul.sportsmanage.domain.Gender;
import goinmul.sportsmanage.domain.Match;
import goinmul.sportsmanage.domain.Sports;
import goinmul.sportsmanage.domain.SportsInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SportsService {

    public SportsInfo getSportsInfo(String sportsName){
        SportsInfo sportsInfo = new SportsInfo();
        sportsInfo.setIcon(sportsInfo.getIconMap().get(sportsName));
        sportsInfo.setInitialName(sportsInfo.getInitialMap().get(sportsName));
        return sportsInfo;
    }

    public Gender createGenderEnum(String gender){
        Gender genderEnum = null;
        switch (gender){
            case "MALE":
                genderEnum = Gender.MALE;
                break;
            case "FEMALE":
                genderEnum = Gender.FEMALE;
                break;
            case "BOTH":
                genderEnum = Gender.BOTH;
                break;
            default:
                break;
        }
        return genderEnum;
    }


    public Sports createSportsEnum(String sports){
        Sports sportsEnum = null;
        switch (sports){
            case "futsal":
                sportsEnum = Sports.FUTSAL;
                break;
            case "tennis":
                sportsEnum = Sports.TENNIS;
                break;
            case "basketball":
                sportsEnum = Sports.BASKETBALL;
                break;
            case "badminton":
                sportsEnum = Sports.BADMINTON;
                break;
            case "billiard":
                sportsEnum = Sports.BILLIARD;
                break;
            case "bowling":
                sportsEnum = Sports.BOWLING;
                break;
            case "soccer":
                sportsEnum = Sports.SOCCER;
                break;
            case "pingpong":
                sportsEnum = Sports.PINGPONG;
                break;
            default:
                break;
        }
        return sportsEnum;
    }

    public Match createMatchEnum(String match){
        Match matchEnum = null;
        switch (match){
            case "SOCIAL":
                matchEnum = Match.SOCIAL;
                break;
            case "TEAM":
                matchEnum = Match.TEAM;
                break;
            case "MERCENARY":
                matchEnum = Match.MERCENARY;
                break;
            default:
                break;
        }
        return matchEnum;
    }





}
