package goinmul.sportsmanage.repository.mercenaryMatch;

import goinmul.sportsmanage.domain.mercenaryMatch.MercenaryMatchTeamMercenaryMaxSize;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MercenaryMatchTeamMercenaryMaxSizeRepository {

    private final EntityManager em;

    public void save(MercenaryMatchTeamMercenaryMaxSize mercenaryTeam) {
        em.persist(mercenaryTeam);
    }

    public MercenaryMatchTeamMercenaryMaxSize findOne(Long id) {
        return em.find(MercenaryMatchTeamMercenaryMaxSize.class, id);
    }

    public List<MercenaryMatchTeamMercenaryMaxSize> findByMercenaryMatchId(Long mercenaryMatchId) {
        return em.createQuery("select m from MercenaryMatchTeamMercenaryMaxSize m" +
                " where m.mercenaryMatch = :mercenaryMatchId", MercenaryMatchTeamMercenaryMaxSize.class)
                .setParameter("mercenaryMatchId", mercenaryMatchId)
                .getResultList();
    }

    public List<MercenaryMatchTeamMercenaryMaxSize> findAll() {
        return em.createQuery("select m from MercenaryMatchTeamMercenaryMaxSize m", MercenaryMatchTeamMercenaryMaxSize.class).getResultList();
    }

    public List<MercenaryMatchTeamMercenaryMaxSize> findWithTeamByMercenaryMatchIdIn(List<Long> mercenaryMatchIdList) {
        return em.createQuery("select m from MercenaryMatchTeamMercenaryMaxSize m join fetch m.team" +
                        " where m.mercenaryMatch.id IN :id", MercenaryMatchTeamMercenaryMaxSize.class)
                .setParameter("id", mercenaryMatchIdList)
                .getResultList();
    }

    public List<MercenaryMatchTeamMercenaryMaxSize> findWithTeamByMercenaryMatchId(Long mercenaryMatchId) {
        return em.createQuery("select m from MercenaryMatchTeamMercenaryMaxSize m join fetch m.team" +
                        " where m.mercenaryMatch.id = :id", MercenaryMatchTeamMercenaryMaxSize.class)
                .setParameter("id", mercenaryMatchId)
                .getResultList();
    }



}
