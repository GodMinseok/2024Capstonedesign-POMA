package goinmul.sportsmanage.repository.teamMatch;


import goinmul.sportsmanage.domain.Gender;
import goinmul.sportsmanage.domain.teamMatch.Team;
import goinmul.sportsmanage.domain.teamMatch.TeamUser;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TeamUserRepository {

    private final EntityManager em;

    public void save(TeamUser teamUser) {
        em.persist(teamUser);
    }

    public TeamUser findOne(Long id) {
        return em.find(TeamUser.class, id);
    }

    public List<TeamUser> findAll() {
        return em.createQuery("select t from TeamUser t", TeamUser.class).getResultList();
    }

    public List<TeamUser> findWithUserAndTeamByTeamMatchIdIn(List<Long> teamMatchIdList) {
        return em.createQuery("select t from TeamUser t join fetch t.user join fetch t.user.team" +
                        " where t.teamMatch.id IN :id", TeamUser.class)
                .setParameter("id", teamMatchIdList)
                .getResultList();
    }

    public List<TeamUser> findWithUserByTeamMatchIdAndGender(Long teamMatchId, Gender gender) {
        List<TeamUser> teamUsers = new ArrayList<>();
        //모집 성별 혼성이면 모든 성별 포함하여 조회
        if (gender.equals(Gender.BOTH)) {
            teamUsers = em.createQuery("select t from TeamUser t join fetch t.user" +
                            " where t.teamMatch.id = : teamMatchId", TeamUser.class)
                    .setParameter("teamMatchId", teamMatchId)
                    .getResultList();
        } else {//남자, 여자 멤버 조회
            teamUsers = em.createQuery("select t from TeamUser t join fetch t.user" +
                            " where t.teamMatch.id = : teamMatchId AND t.user.gender = :gender", TeamUser.class)
                    .setParameter("teamMatchId", teamMatchId)
                    .setParameter("gender", gender)
                    .getResultList();
        }
        return teamUsers;
    }

    public List<TeamUser> findWithTeamWithTeamMatchWithReservationWithGroundByUserId(Long userId) {
        return em.createQuery("select t from TeamUser t join fetch t.team join fetch t.teamMatch join fetch t.teamMatch.reservation join fetch t.teamMatch.reservation.ground " +
                        "where t.user.id =: userId", TeamUser.class)
                .setParameter("userId", userId)
                .getResultList();
    }



}
