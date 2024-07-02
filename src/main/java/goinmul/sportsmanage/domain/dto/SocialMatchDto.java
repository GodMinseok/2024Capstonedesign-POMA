package goinmul.sportsmanage.domain.dto;

import goinmul.sportsmanage.domain.Gender;
import goinmul.sportsmanage.domain.Reservation;
import goinmul.sportsmanage.domain.Sports;
import goinmul.sportsmanage.domain.socialMatch.SocialMatch;
import goinmul.sportsmanage.domain.socialMatch.SocialUser;
import goinmul.sportsmanage.domain.teamMatch.TeamMatch;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;

public class SocialMatchDto {

    private Long id;
    private Integer maxSize;

    private Gender gender;

    private Reservation reservation;

    private Sports sports;

    private List<SocialUser> socialUsers = new ArrayList<>();

    private List<Long> userIdList = new ArrayList<>();

    public SocialMatchDto(SocialMatch socialMatch) {
        id = socialMatch.getId();
        maxSize = socialMatch.getMaxSize();
        gender = socialMatch.getGender();
        reservation = socialMatch.getReservation();
        sports = socialMatch.getSports();
        socialUsers = socialMatch.getSocialUsers();

    }

    public void addUserId(List<Long> userIdList){
        this.userIdList.addAll(userIdList);
    }

}
