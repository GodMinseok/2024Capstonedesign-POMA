package goinmul.sportsmanage.service.mercenaryMatch;


import goinmul.sportsmanage.domain.mercenaryMatch.MercenaryMatchTeamMercenaryMaxSize;
import goinmul.sportsmanage.repository.mercenaryMatch.MercenaryMatchTeamMercenaryMaxSizeRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MercenaryMatchTeamMercenaryMaxSizeService {

    private final EntityManager em;
    private final MercenaryMatchTeamMercenaryMaxSizeRepository mercenaryMatchTeamMercenaryMaxSizeRepository;

    @Transactional
    public void save(MercenaryMatchTeamMercenaryMaxSize mercenaryTeam) {
        mercenaryMatchTeamMercenaryMaxSizeRepository.save(mercenaryTeam);
    }

    public MercenaryMatchTeamMercenaryMaxSize findOne(Long id) {
        return mercenaryMatchTeamMercenaryMaxSizeRepository.findOne(id);
    }

    public List<MercenaryMatchTeamMercenaryMaxSize> findAll() {
        return mercenaryMatchTeamMercenaryMaxSizeRepository.findAll();
    }

    public List<MercenaryMatchTeamMercenaryMaxSize> findMercenaryMatchTeamMercenaryMaxSizeByMercenaryMatchId(Long mercenaryMatchId) {
        return mercenaryMatchTeamMercenaryMaxSizeRepository.findByMercenaryMatchId(mercenaryMatchId);
    }

    public List<MercenaryMatchTeamMercenaryMaxSize> findWithTeamByMercenaryMatchIdIn(List<Long> mercenaryMatchIdList) {
        return em.createQuery("select m from MercenaryMatchTeamMercenaryMaxSize m join fetch m.team" +
                        " where m.mercenaryMatch.id IN :id", MercenaryMatchTeamMercenaryMaxSize.class)
                .setParameter("id", mercenaryMatchIdList)
                .getResultList();
    }




}
