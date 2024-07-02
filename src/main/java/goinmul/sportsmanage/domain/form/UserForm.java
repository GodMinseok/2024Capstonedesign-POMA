package goinmul.sportsmanage.domain.form;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class UserForm {

    private String userId;
    private String name;
    private String password;
    private String phone;
    private LocalDateTime dateRegistered;

}
