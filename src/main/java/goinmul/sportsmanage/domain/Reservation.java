package goinmul.sportsmanage.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Reservation {

    //reservationTime + reservationYmd + groundId 3개를 복합 유니크로 지정해야한다.

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ground_id")
    private Ground ground;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate reservationYmd;

    private int reservationTime;

    public static Reservation createGroundReservation(Ground ground, User user, LocalDate reservationYmd, int reservationTime){
        Reservation groundReservation = new Reservation();
        groundReservation.setGround(ground);
        groundReservation.setUser(user);
        groundReservation.setReservationYmd(reservationYmd);
        groundReservation.setReservationTime(reservationTime);

        return groundReservation;
    }

}
