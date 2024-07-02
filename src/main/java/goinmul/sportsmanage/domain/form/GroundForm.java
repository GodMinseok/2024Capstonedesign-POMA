package goinmul.sportsmanage.domain.form;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter @Setter
public class GroundForm {
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String location;

    private Integer price;

}
