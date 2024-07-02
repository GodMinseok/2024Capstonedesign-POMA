package goinmul.sportsmanage.repository;



import goinmul.sportsmanage.domain.Reservation;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReservationRepository {

    private final EntityManager em;

    public void save(Reservation groundReservation) {
        em.persist(groundReservation);
    }

    public void rollback() {
        em.getTransaction().rollback();
    }

    public Reservation findOne(Long id) {
        return em.find(Reservation.class, id);
    }

    public List<Reservation> findAll() {
        return em.createQuery("select g from Reservation g", Reservation.class).getResultList();
    }
    public List<Reservation> findWithGroundByUserId(Long userId) {
        return em.createQuery("select r from Reservation r join fetch r.ground where r.user.id = :userId", Reservation.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public boolean validateReservation(Long groundId, LocalDate ymd, int time) {
        List<Reservation> reservations = em.createQuery("select r from Reservation r join fetch r.ground where r.ground.id = :groundId and r.reservationYmd =: ymd and r.reservationTime =: time", Reservation.class)
                .setParameter("groundId", groundId)
                .setParameter("ymd", ymd)
                .setParameter("time", time)
                .getResultList();

        return reservations.isEmpty();
    }

}