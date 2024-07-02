package goinmul.sportsmanage.service;


import goinmul.sportsmanage.domain.Reservation;
import goinmul.sportsmanage.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;

    @Transactional
    public boolean saveReservation(Reservation reservation) {
        boolean result = reservationRepository.validateReservation(reservation.getGround().getId(), reservation.getReservationYmd(), reservation.getReservationTime());
        if(result) reservationRepository.save(reservation);
        return result;
    }


}
