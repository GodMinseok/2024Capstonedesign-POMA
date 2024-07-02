package goinmul.sportsmanage.repository.mercenaryMatch;


import goinmul.sportsmanage.domain.Gender;
import goinmul.sportsmanage.domain.Sports;
import goinmul.sportsmanage.domain.mercenaryMatch.MercenaryMatch;
import goinmul.sportsmanage.domain.teamMatch.TeamMatch;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MercenaryMatchRepository {

    private final EntityManager em;

    public void save(MercenaryMatch mercenaryMatch) {
        em.persist(mercenaryMatch);
    }

    public MercenaryMatch findOne(Long id) {
        return em.find(MercenaryMatch.class, id);
    }

    //Select 결과는 여러개지만, 결국엔 MercenaryMatch 객체 하나에 다 들어감.
    public MercenaryMatch findWithMercenaryUserByMercenaryMatchId(Long mercenaryMatchId) {
        MercenaryMatch mercenaryMatch = null;
        List<MercenaryMatch> findMercenaryMatch = em.createQuery("select m from MercenaryMatch m join fetch m.mercenaryMatchUsers where m.id = :mercenaryMatchId", MercenaryMatch.class)
                .setParameter("mercenaryMatchId", mercenaryMatchId)
                .getResultList();
        if(!findMercenaryMatch.isEmpty())
            mercenaryMatch = findMercenaryMatch.get(0);

        return mercenaryMatch;
    }

    public List<MercenaryMatch> findAll() {
        return em.createQuery("select m from MercenaryMatch m", MercenaryMatch.class).getResultList();
    }

    public List<MercenaryMatch> findWithAllEntityByDateAndLocationAndGender(LocalDate date, String location, Gender gender, Sports sports) {

        List<MercenaryMatch> mercenaryMatches;
        if (location.equals("전체") && gender == null) {
            mercenaryMatches = em.createQuery("select m from MercenaryMatch m  join fetch m.mercenaryMatchUsers join fetch m.reservation join fetch m.reservation.user join fetch m.reservation.user.team join fetch m.reservation.ground " +
                            "where m.reservation.reservationYmd = :date AND m.sports =: sports" +
                            " order by m.reservation.reservationTime desc", MercenaryMatch.class)
                    .setParameter("date", date)
                    .setParameter("sports", sports)
                    .getResultList();
        } else if (location.equals("전체")) {
            mercenaryMatches = em.createQuery("select m from MercenaryMatch m  join fetch m.mercenaryMatchUsers join fetch m.reservation join fetch m.reservation.user join fetch m.reservation.user.team join fetch m.reservation.ground " +
                            "where m.reservation.reservationYmd = :date AND m.gender = :gender AND m.sports =: sports" +
                            " order by m.reservation.reservationTime desc", MercenaryMatch.class)
                    .setParameter("date", date)
                    .setParameter("gender", gender)
                    .setParameter("sports", sports)
                    .getResultList();
        } else if (gender == null) {
            mercenaryMatches = em.createQuery("select m from MercenaryMatch m  join fetch m.mercenaryMatchUsers join fetch m.reservation join fetch m.reservation.user join fetch m.reservation.user.team join fetch m.reservation.ground " +
                            "where m.reservation.reservationYmd = :date AND m.reservation.ground.location = :location AND m.sports =: sports" +
                            " order by m.reservation.reservationTime desc", MercenaryMatch.class)
                    .setParameter("date", date)
                    .setParameter("location", location)
                    .setParameter("sports", sports)
                    .getResultList();
        } else {
            mercenaryMatches = em.createQuery("select m from MercenaryMatch m  join fetch m.mercenaryMatchUsers join fetch m.reservation join fetch m.reservation.user join fetch m.reservation.user.team join fetch m.reservation.ground " +
                            "where m.reservation.reservationYmd = :date AND m.reservation.ground.location = :location AND m.gender = :gender AND m.sports =: sports" +
                            " order by m.reservation.reservationTime desc", MercenaryMatch.class)
                    .setParameter("date", date)
                    .setParameter("location", location)
                    .setParameter("gender", gender)
                    .setParameter("sports", sports)
                    .getResultList();
        }
        return mercenaryMatches;

    }





}
