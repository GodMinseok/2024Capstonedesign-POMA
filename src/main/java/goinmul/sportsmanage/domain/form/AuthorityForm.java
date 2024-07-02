package goinmul.sportsmanage.domain.form;

import goinmul.sportsmanage.domain.Authority;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AuthorityForm {
    private Long id;
    private Authority authority;
}
