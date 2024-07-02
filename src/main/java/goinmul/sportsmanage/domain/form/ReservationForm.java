package goinmul.sportsmanage.domain.form;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ReservationForm {

    @NotNull
    private Long groundId;

    @NotNull
    private String date;

    @NotNull
    private int time;

    @NotNull
    private String match;

    @NotNull
    private String gender;

    @NotNull
    private int maxSize;

    private List<Long> userIdList;

    private Integer mercenarySize;

}