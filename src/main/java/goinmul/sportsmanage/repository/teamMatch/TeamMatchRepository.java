package goinmul.sportsmanage.repository.teamMatch;


import goinmul.sportsmanage.domain.Gender;
import goinmul.sportsmanage.domain.Sports;
import goinmul.sportsmanage.domain.dto.TeamMatchDto;
import goinmul.sportsmanage.domain.socialMatch.SocialMatch;
import goinmul.sportsmanage.domain.teamMatch.TeamMatch;
import goinmul.sportsmanage.domain.teamMatch.TeamUser;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TeamMatchRepository {

    private final EntityManager em;

    public void save(TeamMatch teamMatch) {
        em.persist(teamMatch);
    }

    public TeamMatch findOne(Long id) {
        return em.find(TeamMatch.class, id);
    }

    public TeamMatch findWithTeamUserByTeamMatchId(Long id) {
        TeamMatch teamMatch = TeamMatch.createTeamMatch();
        List<TeamMatch> findTeamMatch = em.createQuery("select t from TeamMatch t join fetch t.teamUsers where t.id = :id", TeamMatch.class)
                .setParameter("id", id)
                .getResultList();

        if(!findTeamMatch.isEmpty()){
            teamMatch = findTeamMatch.get(0);
        }

        return teamMatch;

    }

    public List<TeamMatch> findAll() {
        return em.createQuery("select t from TeamMatch t", TeamMatch.class).getResultList();
    }

    //(팀매치-예약-운동장) (팀유저-유저-팀)각각 조인한 걸 합쳐야해서 DTO에 할당함
    public List<TeamMatch> findWithReservationAndGroundByDateAndLocationAndGender(LocalDate date, String location, Gender gender, Sports sports) {

        List<TeamMatch> teamMatches;
        if (location.equals("전체") && gender == null) {
            teamMatches = em.createQuery("select t from TeamMatch t  join fetch t.reservation join fetch t.reservation.ground " +
                            "where t.reservation.reservationYmd = :date AND t.sports =: sports" +
                            " order by t.reservation.reservationTime desc", TeamMatch.class)
                    .setParameter("date", date)
                    .setParameter("sports", sports)
                    .getResultList();
        } else if (location.equals("전체")) {
            teamMatches = em.createQuery("select t from TeamMatch t join fetch t.reservation join fetch t.reservation.ground " +
                            "where t.reservation.reservationYmd = :date AND t.gender = :gender AND t.sports =: sports" +
                            " order by t.reservation.reservationTime desc", TeamMatch.class)
                    .setParameter("date", date)
                    .setParameter("gender", gender)
                    .setParameter("sports", sports)
                    .getResultList();
        } else if (gender == null) {
            teamMatches = em.createQuery("select t from TeamMatch t join fetch t.reservation join fetch t.reservation.ground " +
                            "where t.reservation.reservationYmd = :date AND t.reservation.ground.location = :location AND t.sports =: sports" +
                            " order by t.reservation.reservationTime desc", TeamMatch.class)
                    .setParameter("date", date)
                    .setParameter("location", location)
                    .setParameter("sports", sports)
                    .getResultList();
        } else {
            teamMatches = em.createQuery("select t from TeamMatch t join fetch t.reservation join fetch t.reservation.ground " +
                            "where t.reservation.reservationYmd = :date AND t.reservation.ground.location = :location AND t.gender = :gender AND t.sports =: sports" +
                            " order by t.reservation.reservationTime desc", TeamMatch.class)
                    .setParameter("date", date)
                    .setParameter("location", location)
                    .setParameter("gender", gender)
                    .setParameter("sports", sports)
                    .getResultList();
        }
        return teamMatches;
    }
}
