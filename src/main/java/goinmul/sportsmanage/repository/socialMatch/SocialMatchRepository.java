package goinmul.sportsmanage.repository.socialMatch;


import goinmul.sportsmanage.domain.Gender;
import goinmul.sportsmanage.domain.Sports;
import goinmul.sportsmanage.domain.socialMatch.SocialMatch;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SocialMatchRepository {

    private final EntityManager em;

    public void save(SocialMatch socialMatch) {
        em.persist(socialMatch);
    }

    public SocialMatch findOne(Long id) {
        return em.find(SocialMatch.class, id);
    }

    public List<SocialMatch> findAll() {
        return em.createQuery("select s from SocialMatch s", SocialMatch.class).getResultList();
    }

    //뻥튀기된 데이터로 조인하지만 하이버네이트가 중복 제거함
    public List<SocialMatch> findWithReservationAndGroundByDateAndLocationAndGender(LocalDate date, String location, Gender gender, Sports sports) {

        List<SocialMatch> socialMatches;
        if(location.equals("전체") && gender==null){
            socialMatches = em.createQuery("select s from SocialMatch s join fetch  s.socialUsers join fetch s.reservation join fetch s.reservation.ground " +
                            "where s.reservation.reservationYmd = :date AND s.sports =: sports" +
                            " order by s.reservation.reservationTime desc", SocialMatch.class)
                    .setParameter("date", date)
                    .setParameter("sports", sports)
                    .getResultList();
        }else if(location.equals("전체")){
            socialMatches = em.createQuery("select s from SocialMatch s join fetch  s.socialUsers join fetch s.reservation join fetch s.reservation.ground " +
                            "where s.reservation.reservationYmd = :date AND s.gender = :gender AND s.sports =: sports" +
                            " order by s.reservation.reservationTime desc", SocialMatch.class)
                    .setParameter("date", date)
                    .setParameter("gender", gender)
                    .setParameter("sports", sports)
                    .getResultList();
        } else if(gender==null){
            socialMatches = em.createQuery("select s from SocialMatch s join fetch  s.socialUsers join fetch s.reservation join fetch s.reservation.ground " +
                            "where s.reservation.reservationYmd = :date AND s.reservation.ground.location = :location AND s.sports =: sports" +
                            " order by s.reservation.reservationTime desc", SocialMatch.class)
                    .setParameter("date", date)
                    .setParameter("location", location)
                    .setParameter("sports", sports)
                    .getResultList();
        } else {
            socialMatches = em.createQuery("select s from SocialMatch s join fetch  s.socialUsers join fetch s.reservation join fetch s.reservation.ground " +
                            "where s.reservation.reservationYmd = :date AND s.reservation.ground.location = :location AND s.gender = :gender AND s.sports =: sports" +
                            " order by s.reservation.reservationTime desc", SocialMatch.class)
                    .setParameter("date", date)
                    .setParameter("location", location)
                    .setParameter("gender", gender)
                    .setParameter("sports", sports)
                    .getResultList();
        }
        return socialMatches;
    }

    //결과 행은 여러개지만 결국 소셜 매치 하나에 다 들어가서 리스트 반환아님.
    public SocialMatch findWithSocialUserBySocialMatchId(Long socialMatchId){
        SocialMatch socialMatch = null;
        SocialMatch findSocialMatch = em.createQuery("select s from SocialMatch s join fetch s.socialUsers" +
                        " where s.id = :socialMatchId", SocialMatch.class)
                .setParameter("socialMatchId", socialMatchId)
                .getResultList().get(0);
        socialMatch = findSocialMatch;
        return socialMatch;
    }



}
