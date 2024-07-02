package goinmul.sportsmanage.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.Enumerated;


public enum Authority {
    SUPER_ADMIN, ADMIN, USER;

    @JsonCreator
    public static Authority convert(String authority){
        return Authority.valueOf(authority);
    }
}
