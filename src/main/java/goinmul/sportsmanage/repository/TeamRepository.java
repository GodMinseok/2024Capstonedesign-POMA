package goinmul.sportsmanage.repository;



import goinmul.sportsmanage.domain.teamMatch.Team;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TeamRepository {

    private final EntityManager em;

    public void save(Team team) {
        em.persist(team);
    }

    public Team findOne(Long id) {
        return em.find(Team.class, id);
    }

    public List<Team> findAll(){
        return em.createQuery("select t from Team t", Team.class).getResultList();
    }

    public Team findTeamByName(String name){
        Team team = null;
        List<Team> teams = em.createQuery("select t from Team t where t.name = :name", Team.class)
                .setParameter("name", name).getResultList();

        if(!teams.isEmpty())
            team = teams.get(0);

        return team;
    }

}
