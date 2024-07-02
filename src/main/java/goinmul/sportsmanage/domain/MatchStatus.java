package goinmul.sportsmanage.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public enum MatchStatus {
    RECRUITMENT, DEADLINE
}
